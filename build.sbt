name := "grandata-commons"
version := "1.9.0-SNAPSHOT"
organization := "com.grandata"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.6", "2.11.7")

// Read here for optional jars and dependencies
libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.7",
  "commons-io" % "commons-io" % "2.4",
  "io.jeo" % "jeo" % "0.6",
  "net.sourceforge.jsi" % "jsi" % "1.0.0",
  "com.typesafe" % "config" % "1.2.1",
  "com.google.jimfs" % "jimfs" % "1.0" % "test",
  "org.specs2" %% "specs2-core" % "3.1.1" % "test")

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
//  "jsi.sourceforge.net" at "http://sourceforge.net/projects/jsi/files/m2_repo"
  "jsi.sourceforge.net" at "http://ufpr.dl.sourceforge.net/project/jsi/m2_repo"
)

scalacOptions in Test ++= Seq("-Yrangepos")



credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
publishTo := {
  val url = "https://nexus.grandata.com/content/repositories"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("Grandata Snapshots" at url + "/snapshots")
  else
    Some("Grandata Releases" at url + "/releases")
}
