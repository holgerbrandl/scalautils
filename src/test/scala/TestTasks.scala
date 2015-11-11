import better.files._
import org.scalatest.{FlatSpec, Matchers}

import scalautils.Bash.BashMode
import scalautils.tasks.JobList
import scalautils.tasks.Tasks.{BashSnippet, LsfExecutor, StringOps}

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object TestTasks extends FlatSpec {

  import Matchers._

  val wd = (home / "unit_tests").createIfNotExists(true)


  it should "submit some jobs and wait until they are done " in {
    val tasks = for (i <- 1 to 5) yield {
      BashSnippet( s"""echo "this is task $i" > task_$i.txt """).inDir(wd)
    }

    val runner = new LsfExecutor(joblist = JobList(wd / ".test_tasks"))

    runner.eval(tasks)

    // make sure that outputs have been created

    wd / "task_1.txt" should exist
    wd / "task_5.txt" should exist

    (wd / "task_5.txt").lines shouldBe "this is task $5"
  }

  //  Bash.eval("echo test")(BashMode(beVerbose = true))
  //  Bash.eval("ls")
  //  BashSnippet("ls | grep ammo").inDir(home/"Desktop").eval(new LsfExecutor())


}


class playground extends App {

  val snippet = new BashSnippet("echo hello world $(pwd)").inDir(home / "Desktop")
  snippet.eval


  implicit val bashExecutor = LsfExecutor

  snippet.eval
  snippet.eval(new LsfExecutor)

  println("increasing verbosity")
  implicit val verboseBash = BashMode(beVerbose = true)
  snippet.withName("hello").eval(new LsfExecutor())

  "sdfsdf".toBash.eval

  implicit val jobRunner = LsfExecutor(queue = "short", joblist = JobList(home / "Desktop/"))
  "sleep 300".toBash.eval

  jobRunner.joblist.waitUntilDone()


  private val failedJobs = jobRunner.joblist
}