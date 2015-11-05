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


  import sys.process._

  // notes must be different since otherwise eval's would share the same base-signature
  @Deprecated
  def evalContent(bashSnippet: String) = Seq("/bin/bash", "-c", s"$bashSnippet").!!.trim


  case class BashResult(exitCode: Int, stdout: String, stderr: String)


  // http://stackoverflow.com/questions/15411728/scala-process-capture-standard-out-and-exit-code
  // http://stackoverflow.com/questions/5221524/idiomatic-way-to-convert-an-inputstream-to-a-string-in-scala
  def evalCapture(script: String, showScript: Boolean = true) = {

    if (showScript) println("script:\n" + script.trim)

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


    //    BashResult(f"$script".run(io).exitValue(), out, err)
    BashResult(Seq("/bin/bash", "-c", s"$script").run(io).exitValue(), out, err)
  }


  def eval(script: String, logBase: String = null, showScript: Boolean = true): Int = {
    val logger = if (logBase != null) s"1> $logBase.out.log 2>$logBase.err.log".split(" ")

    if (showScript) println("script:\n" + script.trim)

    Seq("/bin/bash", "-c", s"$script").!
    //    Seq("/bin/bash", "-c", "echo lala").!
  }


  def head(file: File): Unit = {
    s"head ${file.getAbsolutePath}" !
  }
}
