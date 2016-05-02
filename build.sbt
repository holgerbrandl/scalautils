name := "scalautils"
organization := "de.mpicbg.scicomp"
version := "0.3-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.14.0"
//libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.13.0-SNAPSHOT"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))


// bintray upload path is de/mpicbg/scicomp/scalautils_2.11/0.2
// jcenter path https://jcenter.bintray.com/de/mpicbg/scicomp/scalautils_2.11/