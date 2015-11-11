package scalautils.tasks

import better.files.File

import scalautils.Bash

/**
  * A scala reimplemenation of  https://raw.githubusercontent.com/holgerbrandl/datautils/master/bash/lsf_utils.sh
  *
  * @author Holger Brandl
  */
object LsfUtils {


  def wait4jobs(joblist: File = File(".jobs"), msg: String) = {
    Bash.eval(s"""${changeWdOptional(joblist.parent)} wait4jobsReport $joblist""")

    if (msg != null) Bash.eval(s"mailme $msg")
  }


  def bsub(name: String, cmd: String, joblist: JobList = new JobList(".jobs"), numCores: Int = 1, queue: String = "short", otherArgs: String = "", workingDirectory: File = null) = {
    val threadArg = if (numCores > 1) s"-R span[hosts=1] -n $numCores" else ""

    val job = s"""${changeWdOptional(workingDirectory)} mysub $name "$cmd" -q $queue $threadArg $otherArgs| joblist $joblist"""

    //    job
    Bash.evalCapture(job).stdout
  }


  private def changeWdOptional(wd: File): String = {
    if (wd != null && wd != File(".")) "cd " + wd.fullPath + "; " else ""
  }


  def mailme(subject: String, body: String = "", logSubject: Boolean = true) = {
    if (logSubject) Console.err.println(s"$subject")

    //    val subject = "test"
    //    val body = """
    //    hello world
    //    more content""".stripLeadingWS

    Bash.eval(s"""echo -e 'Subject:$subject\n\n $body' | sendmail $$(whoami)@mpi-cbg.de > /dev/null""")
  }
}


object test extends App {

  scalautils.tasks.LsfUtils.mailme("test", "hallo holger")
}