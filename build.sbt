

name := "scalautils"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.13.0"
//libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.13.0-SNAPSHOT"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"


//if(sys.env.get("USE_A49").isDefined){
//}else{
//  initialCommands in (Test, console) := """"""
//}

//sys.env.get("TERM_PROGRAM").isDefined match {
//  case true => {
//    libraryDependencies += "com.lihaoyi" % "ammonite-repl" % "0.4.9-SNAPSHOT" % "test" cross CrossVersion.full
//    initialCommands in(Test, console) := """ammonite.repl.Repl.run("")"""
//  }
//  case false => libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
//}

// use custom ammonite build on macos
sys.env.get("TERM_PROGRAM").isDefined match{
  case true => libraryDependencies += "com.lihaoyi" % "ammonite-repl" % "0.4.9-SNAPSHOT" % "test" cross CrossVersion.full
  case false => libraryDependencies += "com.lihaoyi" % "ammonite-repl" % "0.4.8" % "test" cross CrossVersion.full
}


initialCommands in (Test, console) := """ammonite.repl.Repl.run("")"""

