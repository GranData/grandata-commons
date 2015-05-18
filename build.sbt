name := "grandata-commons"

version := "1.0"

scalaVersion := "2.10.4"

// Read here for optional jars and dependencies
libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.7",
  "commons-io" % "commons-io" % "2.4",
  "io.jeo" % "jeo" % "0.6",
  "net.sourceforge.jsi" % "jsi" % "1.0.0",
  "com.google.jimfs" % "jimfs" % "1.0" % "test",
  "org.specs2" %% "specs2-core" % "3.1.1" % "test")

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "jsi.sourceforge.net" at "http://sourceforge.net/projects/jsi/files/m2_repo")

scalacOptions in Test ++= Seq("-Yrangepos")