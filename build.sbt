import Dependencies._

lazy val application = project.settings(
  libraryDependencies ++= Seq(jodaTime, jodaConvert, scalaTest)
)

lazy val web = project.dependsOn(application).settings(
  libraryDependencies ++= Seq(unfilteredFilter, servletApi)
)

lazy val root = project.in(file(".")).settings(
  libraryDependencies ++= Seq(unfilteredJetty),
  cancelable := true
).dependsOn(web).aggregate(application, web)

organization in ThisBuild := "com.hipstercoffee"

version in ThisBuild := "0.3.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.10.4"
