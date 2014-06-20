import Dependencies._

import com.typesafe.sbt.SbtGit._
import releasenotes._

versionWithGit

showCurrentGitBranch

git.baseVersion := "0.5.0"

lazy val baseName = "hipster-coffee-shop"

lazy val application = project
  .settings(
    normalizedName := s"$baseName-${normalizedName.value}",
    libraryDependencies ++= Seq(
      scalazCore,
      jodaTime,
      jodaConvert,
      jodaMoney,
      scalaTest))

lazy val web = project
  .dependsOn(application)
  .settings(
    normalizedName := s"$baseName-${normalizedName.value}",
    libraryDependencies ++= Seq(
      scalazCore,
      unfilteredFilter,
      argonaut,
      argonautUnfiltered,
      servletApi))

lazy val root = project
  .in(file("."))
  .settings(
    normalizedName := baseName,
    name := "Hipster Coffee Shop",
    libraryDependencies ++= Seq(unfilteredJetty))
  .dependsOn(web)
  .aggregate(application, web)
  .settings(Revolver.settings: _*)
  .enablePlugins(ReleaseNotesPlugin)

organization in ThisBuild := "com.hipstercoffee"

scalaVersion in ThisBuild := "2.10.4"
