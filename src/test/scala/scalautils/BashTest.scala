package scalautils

import scalautils.Bash._

/**
  * @author Holger Brandl
  */
object BashTest extends App {

  //  import scalautils.Bash
  Bash.eval("""cd ~/unit_tests
  echo baum > baum.txt
  echo haus > haus.txt""")
  Bash.eval("cd ~/unit_tests\n echo baum > baum2.txt\necho haus > haus2.txt")


  val jobCmed = "cd '/home/brandl/unit_tests'; mysub \"Users__brandl__633224592__1112201512d4102\" 'cd /home/brandl/unit_tests ## lsfutils:change into wd\nsleep 60; echo \"this is task 1\" > task_1.txt ' -q short   | joblist /home/brandl/unit_tests/.test_tasks"
  Bash.eval(jobCmed)
}


object BashPlayground {

  import better.files.File

  import scala.language.postfixOps
  import scalautils._
  import sys.process._


  //http://oldfashi\onedsoftware.com/2009/07/10/scala-code-review-foldleft-and-foldright/
  List("/bin/bash", "-c", s"'kaka'").foldLeft("")((b, a) => b + " " + a).trim


  def R(rcmd: String) {
    Seq("/bin/bash", "-c", s"echo '$rcmd' | Rscript --vanilla -") !
  }


  Bash.eval("echo test 1>&2",
    redirectStderr = File("/Users/brandl/Desktop/stderr_redir.txt"),
    redirectStdout = File("/Users/brandl/Desktop/stdout_redir.txt"))
  Bash.eval("which ls", redirectStdout = File("/Users/brandl/Desktop/stdin_redir.txt"))

  //http://docs.scala-lang.org/tutorials/tour/operators.html
  //Any method which takes a single parameter can be used as an infix operator in Scala. x
  //R "1+1"
  R("1+1")

  File("/home/brandl/.bash_profile") // .head

  //import scala.sys.process._

  //val cmd = "uname -a" // Your command
  //val output = cmd.!!.trim // Captures the output

  // or
  // Process("cat temp.txt")!

  eval("om $(pwd)")
}
