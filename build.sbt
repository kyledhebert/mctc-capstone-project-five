name := """project-five"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

// Resolver is needed only for SNAPSHOT versions
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.avaje.ebeanorm" % "avaje-ebeanorm" % "7.3.1",
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3-SNAPSHOT"
)

routesGenerator := InjectedRoutesGenerator