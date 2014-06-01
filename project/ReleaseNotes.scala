package releasenotes

import sbt._
import Keys._
import Def.Setting
import sbt.complete.DefaultParsers._
import sbt.complete.Parser
import scala.util.matching.Regex
import xsbt.api
import xsbt.api.ShowAPI

object ReleaseNotes {

  case class Contributor(name: String, email: String, commits: Int)
  case class Release(version: SemVersion, gitTag: String)
  case class SemVersion(major: Int, minor: Int, patch: Int)
  object SemVersion {
    implicit val shows = new api.Show[SemVersion] {
      override def show(v: SemVersion): String = s"${v.major}.${v.minor}.${v.patch}"
    }
  }
  case class DiffStats(filesChanged: Int = 0, insertions: Int = 0, deletions: Int = 0)
  case class Ticket(id: Int, description: String)
  case class ReleaseNotes(
    release: Release,
    prevRelease: Option[Release],
    diffStats: DiffStats,
    closedTickets: List[Ticket],
    contributors: List[Contributor])

  object ReleaseNotesKeys {
    lazy val releaseNotesReleases =
      taskKey[List[Release]]("Get info on all releases of the project")
    lazy val generateReleaseNotes =
      inputKey[ReleaseNotes]("Generates release notes for the specified release")
    lazy val dumpReleaseNotes =
      inputKey[File]("Dumps the release notes for the specified release as a markdown file")
    lazy val releaseNotesDir =
      settingKey[File]("The directory to which the release notes are written")
    lazy val releaseNotesFileName =
      settingKey[Release => String]("Function that returns the file name of the release note")
    lazy val releaseNotesTemplate =
      settingKey[ReleaseNotes => String]("Function that returns the markdown release notes as a string")
  }

  import ReleaseNotesKeys._

  lazy val releaseNotesSettings: Seq[Setting[_]] = Seq(
    Defaults.releasesTask,
    Defaults.generateReleaseNotesTask,
    Defaults.dumpReleaseNotesTask,
    Defaults.dir,
    Defaults.fileName,
    Defaults.template
  )

  object Defaults {
    def releasesTask = releaseNotesReleases := Implementation.releases
    def generateReleaseNotesTask = generateReleaseNotes := {
      val (release, prevRelease) = Implementation.releaseParser.parsed
      Implementation.releaseNotes(prevRelease, release)
    }
    def dumpReleaseNotesTask = dumpReleaseNotes := {
      val releaseNotes = ReleaseNotesKeys.generateReleaseNotes.parsed.value
      val fileName = releaseNotesFileName.value(releaseNotes.release)
      val outputFile = releaseNotesDir.value / fileName
      IO.write(outputFile, releaseNotesTemplate.value(releaseNotes))
      outputFile
    }
    def dir =
      releaseNotesDir := target.value / "release-notes"
   def fileName = releaseNotesFileName := {
      release => s"${normalizedName.value}-${ShowAPI.show(release.version)}.md"
    }
   def template = releaseNotesTemplate := {
      releaseNotes =>
        def contributorMessage(c: Contributor) =
          s"- __${c.name}__ contributed with __${c.commits}__ commit(s)."
        def contributors(cs: List[Contributor]) =
          cs map contributorMessage mkString "\n"
        def tickets(ts: List[Ticket]) =
          ts map (t => s"- __${t.id}:__ ${t.description}") mkString "\n"

        s"""
         |#RELEASE NOTES for ${name.value} ${ShowAPI.show(releaseNotes.release.version)}
         |
         |## Tickets fixed in this release
         |
         |${tickets(releaseNotes.closedTickets)}
         |
         |##Stats
         |
         |- __Files changed:__ ${releaseNotes.diffStats.filesChanged}
         |- __Insertions (+):__ ${releaseNotes.diffStats.insertions}
         |- __Deletions (+):__ ${releaseNotes.diffStats.deletions}
         |- __Number of committers:__ ${releaseNotes.contributors.size}
         |
         |##Contributors
         |
         |${contributors(releaseNotes.contributors)}
         |
       """.stripMargin
    }
  }

  private object Implementation {
    val releaseParser: Parser[(Release, Option[Release])] = {
      val releases = Implementation.releases
      val tags = releases map (_.gitTag)
      val parser = token(StringBasic).examples(tags.toSet, check = true)
      token(Space) ~> parser map { releaseTag =>
        releases.dropWhile(_.gitTag != releaseTag) match {
          case head :: tail => (head, tail.headOption)
          case _ => sys.error("No release to generate release notes for")
        }
      }
    }

    def releaseNotes(
      prevRelease: Option[Release],
      release: Release) = {
      val stats = diffStats(prevRelease, release)
      val cs = contributors(prevRelease, release)
      val ts = closedTickets(prevRelease, release)
      ReleaseNotes(release, prevRelease, stats, ts, cs)
    }

    def contributors(
        prevRelease: Option[Release],
        release: Release): List[Contributor] = {
      val prev = prevCommit(prevRelease)
      val contribs = Process(s"git shortlog -sne $prev${release.gitTag}")
      contribs.lines.toList collect {
        case GitContributor(c) => c
      }
    }

    def diffStats(
        prevRelease: Option[Release],
        release: Release): DiffStats = {
      val prev = prevCommit(prevRelease)
      val diffOutput = Process(s"git diff --shortstat $prev${release.gitTag}")
      diffOutput.lines.head match {
        case GitDiffStats(stats) => stats
        case x => sys.error(s"Cannot parse git output as DiffStats: $x")
      }
    }

    def closedTickets(
      prevRelease: Option[Release],
      release: Release): List[Ticket] = {
      val prev = prevCommit(prevRelease)
      val ticketsOutput = Process(s"git log --format=%s $prev${release.gitTag}")
      ticketsOutput.lines.toList collect {
        case GitTicket(ticket) => ticket
      }
    }

    def releases: List[Release] = availableTags.reverse collect {
      case tag @ GitSemVersion(v) => Release(v, tag)
    }
    private def availableTags: List[String] =
      Process(s"git tag").lines.toList

    private def prevCommit(prevRelease: Option[Release]): String =
      prevRelease.fold("")(r => s"${r.gitTag}..")
  }

  private implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  private object GitSemVersion {
    private val SemVerMatcher = """(\d+)\.(\d+).(\d+)""".r
    def unapply(versionStr: String): Option[SemVersion] = versionStr match {
      case r"v(\d+)$major\.(\d+)$minor.(\d+)$patch" =>
        Some(SemVersion(major.toInt, minor.toInt, patch.toInt))
      case _ =>
        None
    }
  }

  private object GitContributor {
    def unapply(s: String): Option[Contributor] = s match {
      case r"\s+(\d+)$commits\s+(\w[\w\s]+\w)$name\s+<(.*)$email>" =>
        Some(Contributor(name, email, commits.toInt))
      case _ =>
        None
    }
  }

  private object GitDiffStats {
    def unapply(s: String): Option[DiffStats] = {
      s.trim match {
        case r"(\d+)$changedFiles files changed, (\d+)$insertions insertion(s?)$i\(\+\), (\d+)$deletions deletion(s?)$d\(-\)" =>
          Some(DiffStats(changedFiles.toInt, insertions.toInt, deletions.toInt))
        case _ =>
          None
      }
    }
  }

  private object GitTicket {
    def unapply(s: String): Option[Ticket] = {
      s.trim match {
        case r"([^\[]+)$message\[closes #(\d+)$ticketId\]" =>
          Some(Ticket(ticketId.toInt, message))
        case x =>
          None
      }
    }
  }

}
