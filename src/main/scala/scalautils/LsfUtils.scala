package scalautils

import scalautils.Tasks.JobList

/**
  * A scala reimplemenation of  https://raw.githubusercontent.com/holgerbrandl/datautils/master/bash/lsf_utils.sh
  *
  * @author Holger Brandl
  */
object LsfUtils {

  def wait4jobs(joblist: String = ".jobs", msg: String, wd: java.io.File = null) = {
    Bash.eval( s"""${changeWdOptional(wd)} wait4jobs $joblist""")

    if (msg != null) Bash.eval(s"mailme $msg")
  }


  def bsub(name: String, cmd: String, joblist: JobList = new JobList(".jobs"), numCores: Int = 1, queue: String = "short", otherArgs: String = "", workingDirectory: java.io.File = null) = {
    val threadArg = if (numCores > 1) s"-R span[hosts=1] -n $numCores" else ""

    val job = s"""${changeWdOptional(workingDirectory)} mysub $name "$cmd" -q $queue $threadArg $otherArgs| joblist $joblist"""

    //    job
    Bash.evalCapture(job).stdout
  }


  private def changeWdOptional(wd: java.io.File): String = {
    if (wd != null) "cd " + wd.getAbsolutePath + "; " else ""
  }
}
