package scalautils.tasks

import java.util.concurrent.Executors

import better.files._

import scala.concurrent.ExecutionContext
import scalautils.Bash

/**
  * Slim layer around bsub & friends
  *
  * @author Holger Brandl
  */
object Tasks extends App {


  abstract class SnippetExecutor {

    def eval(bashSnippet: BashSnippet)


    def eval(tasks: Iterable[BashSnippet]) = {
      tasks.foreach(_.eval(this))
    }
  }


  class LocalShell extends SnippetExecutor {

    override def eval(bashSnippet: BashSnippet): Unit = {
      val status = Bash.eval(bashSnippet.cmd)
      println(status)
    }


    def eval(tasks: Iterable[BashSnippet], numThreads: Int = Runtime.getRuntime.availableProcessors()): Unit = {
      implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(numThreads))

      tasks.par.foreach(_.eval(this))
    }
  }


  class DryRunExecutor extends SnippetExecutor {

    override def eval(bashSnippet: BashSnippet): Unit = {
      println(s"---running: ${bashSnippet.name} ---")
      println(bashSnippet.cmd)
      println("----------------")
    }
  }


  case class LsfExecutor(numThreads: Int = 1, queue: String = "short",
                         joblist: JobList = JobList(), wd: File = null) extends SnippetExecutor {

    // more r like default would be http://stackoverflow.com/questions/3090753/is-it-possible-for-an-optional-argument-value-to-depend-on-another-argument-in-s
    def wd_ = if (wd != null) wd else joblist.file.parent


    override def eval(tasks: Iterable[BashSnippet]): Unit = {
      super.eval(tasks)
      joblist.waitUntilDone()
    }


    override def eval(bashSnippet: BashSnippet): Unit = {
      LsfUtils.bsub(bashSnippet.name, bashSnippet.cmd, joblist = joblist)
    }
  }


  case class BashSnippet(cmd: String, name: String = "") {

    def name_ = if (this.name.isEmpty) "snippet_" + cmd.hashCode.toString else this.name


    //http://stackoverflow.com/questions/7249396/how-to-clone-a-case-class-instance-and-change-just-one-field-in-scala
    def withName(name: String): BashSnippet = this.copy(name = name)


    def inDir(wd: File) = this.copy(cmd = s"cd ${wd.path}\n" + cmd)


    def eval(implicit snippetEvaluator: SnippetExecutor = new LocalShell()) = {
      snippetEvaluator.eval(this)
    }
  }


  implicit class StringOps(str: String) {

    def toBash: BashSnippet = BashSnippet(str)
  }


}




