import sbt._

object Dependencies {

  val unfilteredVersion = "0.8.0"
  val unfilteredFilter = "net.databinder" %% "unfiltered-filter" % unfilteredVersion
  val unfilteredJetty = "net.databinder" %% "unfiltered-jetty" % unfilteredVersion
  val servletApi = "javax.servlet" % "servlet-api" % "2.3" % "provided"
  
  val jodaTime = "joda-time" % "joda-time" % "2.3"
  val jodaConvert = "org.joda" % "joda-convert" % "1.6"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.1.6" % "test"

}