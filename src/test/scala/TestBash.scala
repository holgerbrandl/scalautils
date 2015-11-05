import java.io.File
import scalautils.Bash._

// todo convert into actual unit tests.
/**
  * Document me!
  *
  * @author Holger Brandl
  */


object TestBashEval {


  eval( """echo test | tr -d 'e'""")
  eval("echo lala")

  eval(
    """
      echo test | tr -d 'e'
      ls
    """.stripMargin)


}


object BashPlayground {

  import sys.process._
  import scala.language.postfixOps

  //http://oldfashionedsoftware.com/2009/07/10/scala-code-review-foldleft-and-foldright/
  List("/bin/bash", "-c", s"'kaka'").foldLeft("")((b, a) => b + " " + a).trim


  def R(rcmd: String) {
    Seq("/bin/bash", "-c", s"echo '$rcmd' | Rscript --vanilla -") !
  }


  //http://docs.scala-lang.org/tutorials/tour/operators.html
  //Any method which takes a single parameter can be used as an infix operator in Scala. x
  //R "1+1"
  R("1+1")

  head(new File("/home/brandl/.bash_profile"))


  //import scala.sys.process._
  //val cmd = "uname -a" // Your command
  //val output = cmd.!!.trim // Captures the output

  // or
  // Process("cat temp.txt")!

  eval("om $(pwd)")
}
