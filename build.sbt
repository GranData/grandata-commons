name := "grandata-commons"

version := "1.0"

scalaVersion := "2.10.4"

// Read here for optional jars and dependencies
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.1.1" % "test")

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// For tests
libraryDependencies += "com.google.jimfs" % "jimfs" % "1.0"

scalacOptions in Test ++= Seq("-Yrangepos")