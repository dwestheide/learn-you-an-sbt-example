import sbt._

object Dependencies {

  val unfilteredVersion = "0.8.0"
  val argonautVersion = "6.0.4"
  val unfilteredFilter = "net.databinder" %% "unfiltered-filter" % unfilteredVersion
  val unfilteredJetty = "net.databinder" %% "unfiltered-jetty" % unfilteredVersion
  val servletApi = "javax.servlet" % "servlet-api" % "2.3" % "provided"
  val argonaut = "io.argonaut" %% "argonaut" % argonautVersion
  val argonautUnfiltered = "io.argonaut" %% "argonaut-unfiltered" % "6.0.4"

  val jodaTime = "joda-time" % "joda-time" % "2.3"
  val jodaConvert = "org.joda" % "joda-convert" % "1.6"
  val jodaMoney = "org.joda" % "joda-money" % "0.9"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.1.6" % "test"

  val scalazCore = "org.scalaz" %% "scalaz-core" % "7.0.5"

}