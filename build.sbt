import sbt.Keys._

name := "scalautils"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.13.0"

//if(sys.env.get("USE_A49").isDefined){
//}else{
//  initialCommands in (Test, console) := """"""
//}
libraryDependencies += "com.lihaoyi" % "ammonite-repl" % "0.4.9-SNAPSHOT" % "test" cross CrossVersion.full
initialCommands in(Test, console) := """ammonite.repl.Repl.run("")"""