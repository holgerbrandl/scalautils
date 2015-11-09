import better.files._

import scalautils.Bash.BashMode
import scalautils.Tasks.{BashSnippet, LsfExecutor, StringOps}

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object TestTasks extends App {


  //  Bash.eval("echo test")(BashMode(beVerbose = true))
  //  Bash.eval("ls")
  //  BashSnippet("ls | grep ammo").inDir(home/"Desktop").eval(new LsfExecutor())

  val snippet = new BashSnippet("echo hello world $(pwd)").inDir(home / "Desktop")
  snippet.eval


  implicit val bashExecutor = LsfExecutor

  snippet.eval
  snippet.eval(new LsfExecutor)

  println("increasing verbosity")
  implicit val verboseBash = BashMode(beVerbose = true)
  snippet.withName("hello").eval(new LsfExecutor())

  "sdfsdf".toBash.eval
}