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
class TestTasks extends FlatSpec with Matchers {

  //  import Matchers._

  val wd = (home / "unit_tests").createIfNotExists(true)


  it should "submit some jobs and wait until they are done " in {
    val tasks = for (i <- 1 to 5) yield {
      BashSnippet(s"""sleep 60; echo "this is task $i" > task_$i.txt """).inDir(wd).withAutoName
    }

    val runner = new LsfExecutor(joblist = JobList(wd / ".test_tasks"))
    runner.joblist.reset

    //    runner.eval(tasks)
    //    tasks.foreach(_.eval(runner))
    tasks.head.eval(runner)
    runner.joblist.waitUntilDone()

    // make sure that outputs have been created
    (wd / ".logs").toJava should exist
    (wd / ".test_tasks").toJava should exist


    (wd / "task_1.txt").toJava should exist
    (wd / "task_5.txt").toJava should exist

    (wd / "task_5.txt").lines shouldBe "this is task $5"
  }

  //  Bash.eval("echo test")(BashMode(beVerbose = true))
  //  Bash.eval("ls")
  //  BashSnippet("ls | grep ammo").inDir(home/"Desktop").eval(new LsfExecutor())
}


class playground extends App {

  BashSnippet(s"""sleep 60; echo "this is task 22" > task_22.txt """).inDir(root / "foo").inDir(home / "unit_tests").withAutoName

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