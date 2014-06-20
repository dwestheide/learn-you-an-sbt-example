package releasenotes

import sbt.{Plugins, Def, AutoPlugin}

object ReleaseNotesPlugin extends AutoPlugin {
  override def projectSettings: Seq[Def.Setting[_]] = ReleaseNotes.releaseNotesSettings
}
