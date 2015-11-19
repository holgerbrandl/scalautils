name := "scalautils"
organization := "de.mpicbg.scicomp"
version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.13.0"
//libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.13.0-SNAPSHOT"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

// use custom ammonite build on macos
libraryDependencies += "com.lihaoyi" % "ammonite-repl" % "0.4.9" % "test" cross CrossVersion.full


initialCommands in(Test, console) := """ammonite.repl.Repl.run("")"""

