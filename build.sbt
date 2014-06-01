import Dependencies._

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

showCurrentGitBranch

organization in ThisBuild := "com.hipstercoffee"

version in ThisBuild := "0.4.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.10.4"
