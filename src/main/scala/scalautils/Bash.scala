package scalautils

/**
  * Utilities to ease the integration of bash into scala-programs
  *
  * @author Holger Brandl
  */


import java.io.File

import scala.language.postfixOps
import scala.sys.process._

/**
  * http://www.scala-lang.org/api/current/index.html#scala.sys.process.package
  * sbt version of it
  * http://www.scala-sbt.org/0.13/docs/Process.html
  */
// todo add implicit to allow for """find | grep sdf >test.df"""bash
object Bash {


  case class BashMode(beVerbose: Boolean = false, dryRun: Boolean = false)

  import sys.process._

  // notes must be different since otherwise eval's would share the same base-signature
  @Deprecated
  def evalContent(bashSnippet: String) = Seq("/bin/bash", "-c", s"$bashSnippet").!!.trim


  case class BashResult(exitCode: Int, stdout: String, stderr: String)


  // http://stackoverflow.com/questions/15411728/scala-process-capture-standard-out-and-exit-code
  // http://stackoverflow.com/questions/5221524/idiomatic-way-to-convert-an-inputstream-to-a-string-in-scala
  def eval(script: String)(implicit mode: BashMode = BashMode()): BashResult = {

    if (mode.beVerbose) println("script:\n" + script.trim)

    //    ("/bin/ls /tmp" run BasicIO(false, None, None)).exitValue

    var err = ""
    var out = ""

    val io = new ProcessIO(
      stdin => None,
      stdout => {
        out = scala.io.Source.fromInputStream(stdout).mkString
        stdout.close()
      },
      stderr => {
        err = scala.io.Source.fromInputStream(stderr).mkString
        stderr.close()
      })


    if (mode.dryRun)
      return null

    //    BashResult(f"$script".run(io).exitValue(), out, err)
    BashResult(Seq("/bin/bash", "-c", s"$script").run(io).exitValue(), out, err)
  }


  def evalStatus(script: String, logBase: String = null)(implicit mode: BashMode = BashMode()): Int = {
    val logger = if (logBase != null) s"1> $logBase.out.log 2>$logBase.err.log".split(" ")

    if (mode.beVerbose) println("script:\n" + script.trim)

    if (mode.dryRun)
      return 1000

    Seq("/bin/bash", "-c", s"$script").!
    //    Seq("/bin/bash", "-c", "echo lala").!
  }


  def head(file: File): Unit = {
    s"head ${file.getAbsolutePath}" !
  }
}


object BashTest extends App {

  //  import scalautils.Bash
  Bash.eval("""cd ~/unit_tests
  echo baum > baum.txt
  echo haus > haus.txt""")
  Bash.eval("cd ~/unit_tests\n echo baum > baum2.txt\necho haus > haus2.txt")


  val jobCmed = "cd '/home/brandl/unit_tests'; mysub \"Users__brandl__633224592__1112201512d4102\" 'cd /home/brandl/unit_tests ## lsfutils:change into wd\nsleep 60; echo \"this is task 1\" > task_1.txt ' -q short   | joblist /home/brandl/unit_tests/.test_tasks"
  Bash.eval(jobCmed)


}