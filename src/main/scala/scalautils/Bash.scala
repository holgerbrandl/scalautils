package scalautils

/**
  * Utilities to ease the integration of bash into scala-programs
  *
  * @author Holger Brandl
  */


import java.io.{BufferedReader, PrintStream}

import scala.language.postfixOps
import scala.sys.process._

/**
  * http://www.scala-lang.org/api/current/index.html#scala.sys.process.package
  * sbt version of it
  * http://www.scala-sbt.org/0.13/docs/Process.html
  */
// todo add implicit to allow for """find | grep sdf >test.df"""bash
object Bash {


  @Deprecated // too complicated api
  case class BashMode(beVerbose: Boolean = false, dryRun: Boolean = false)


  // notes must be different since otherwise eval's would share the same base-signature
  @Deprecated
  def evalContent(bashSnippet: String) = Seq("/bin/bash", "-c", s"$bashSnippet").!!.trim


  case class BashResult(exitCode: Int, stdout: Iterable[String], stderr: Iterable[String]) {
    def sout = stdout.mkString("\n")


    def serr = stderr.mkString("\n")
  }


  // http://stackoverflow.com/questions/15411728/scala-process-capture-standard-out-and-exit-code
  // http://stackoverflow.com/questions/5221524/idiomatic-way-to-convert-an-inputstream-to-a-string-in-scala
  // todo add argument to disable stderr/stdout recording (to prevent memory problems)
  // tooo refactor away mode
  def eval(script: String, showOutput: Boolean = false)(implicit mode: BashMode = BashMode()): BashResult = {

    if (mode.beVerbose) println("script:\n" + script.trim)

    //    ("/bin/ls /tmp" run BasicIO(false, None, None)).exitValue

    var err = ""
    var out = ""

    // todo extract method

    val io = new ProcessIO(
      stdin => None,
      stdout => {
        if (showOutput) {
          out = mkMonitor(scala.io.Source.fromInputStream(stdout).bufferedReader())
        } else {
          out = scala.io.Source.fromInputStream(stdout).mkString
        }

        stdout.close()
      },
      stderr => {
        if (showOutput) {
          err = mkMonitor(scala.io.Source.fromInputStream(stderr).bufferedReader())
        } else {
          err = scala.io.Source.fromInputStream(stderr).mkString
        }

        stderr.close()
      })


    if (mode.dryRun)
      return null

    //    BashResult(f"$script".run(io).exitValue(), out, err)
    BashResult(Seq("/bin/bash", "-c", s"$script").run(io).exitValue(), out.split("\n"), err.split("\n"))
  }


  def mkMonitor(allReader: BufferedReader, monitorStream: PrintStream = Console.out) = {
    // adopted from scala.io.BufferedSource.mkString
    val sb = new StringBuilder
    val buf = new Array[Char](scala.io.Source.DefaultBufSize)
    var n = 0

    while (n != -1) {
      n = allReader.read(buf)
      monitorStream.print(buf)
      if (n > 0) sb.appendAll(buf, 0, n)
    }

    sb.result
  }


  def evalStatus(script: String, logBase: String = null)(implicit mode: BashMode = BashMode()): Int = {
    val logger = if (logBase != null) s"1> $logBase.out.log 2>$logBase.err.log".split(" ")

    if (mode.beVerbose) println("script:\n" + script.trim)

    if (mode.dryRun)
      return 1000

    Seq("/bin/bash", "-c", s"$script").!
    //    Seq("/bin/bash", "-c", "echo lala").!
  }

}


