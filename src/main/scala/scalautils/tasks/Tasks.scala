package scalautils.tasks

import better.files._

import scalautils.Bash

/**
  * Slim layer around bsub & friends
  *
  * @author Holger Brandl
  */
object Tasks extends App {


  trait SnippetExecutor {

    def eval(bashSnippet: BashSnippet)
  }


  class LocalShell extends SnippetExecutor {

    override def eval(bashSnippet: BashSnippet): Unit = {
      val status = Bash.eval(bashSnippet.cmd)
      println(status)
    }
  }


  case class JobList(file: File = File(".joblist")) extends AnyRef {


    def waitUntilDone(msg: String = "") = LsfUtils.wait4jobs(this.file, msg)


    def this(name: String) = this(File(name))
  }


  case class LsfExecutor(numThreads: Int = 1, queue: String = "medium",
                         joblist: JobList = JobList(), wd: File = null) extends SnippetExecutor {

    // more r like default would be http://stackoverflow.com/questions/3090753/is-it-possible-for-an-optional-argument-value-to-depend-on-another-argument-in-s
    def wd_ = if (wd != null) wd else joblist.file.parent


    override def eval(bashSnippet: BashSnippet): Unit = {
      LsfUtils.bsub(bashSnippet.name, bashSnippet.cmd, joblist = joblist)
    }
  }


  case class BashSnippet(cmd: String, name: String = "") {

    def name_ = if (this.name.isEmpty) cmd.hashCode.toString else this.name


    //http://stackoverflow.com/questions/7249396/how-to-clone-a-case-class-instance-and-change-just-one-field-in-scala
    def withName(name: String): BashSnippet = this.copy(name = name)


    def inDir(wd: File) = this.copy(cmd = s"cd ${wd.path}\n" + cmd)


    def eval(implicit snippetEvaluator: SnippetExecutor = new LocalShell()) = {
      println(s"using ${snippetEvaluator.getClass.getName}")
      snippetEvaluator.eval(this)
    }
  }


  implicit class StringOps(str: String) {

    def toBash: BashSnippet = BashSnippet(str)
  }


}


