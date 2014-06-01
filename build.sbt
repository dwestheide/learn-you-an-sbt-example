import Dependencies._

lazy val application = project
  .settings(
    libraryDependencies ++= Seq(
      scalazCore,
      jodaTime,
      jodaConvert,
      jodaMoney,
      scalaTest))

lazy val web = project
  .dependsOn(application)
  .settings(
    libraryDependencies ++= Seq(
      scalazCore,
      unfilteredFilter,
      argonaut,
      argonautUnfiltered,
      servletApi))

lazy val root = project
  .in(file("."))
  .settings(
    libraryDependencies ++= Seq(unfilteredJetty))
  .dependsOn(web)
  .aggregate(application, web)

organization in ThisBuild := "com.hipstercoffee"

version in ThisBuild := "0.4.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.10.4"
