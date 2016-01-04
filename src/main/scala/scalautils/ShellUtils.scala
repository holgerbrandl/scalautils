package scalautils

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object ShellUtils {

  def mailme(subject: String, body: String = "", logSubject: Boolean = true) = {
    if (logSubject) Console.err.println(s"$subject")

    // use sendmail by default for email reporting but support custom commands as well via a variable
    val defCmd = s"""echo -e 'Subject:$subject\n\n $body' | sendmail $$(whoami)@mpi-cbg.de > /dev/null"""
    val mailCmd = sys.env.getOrElse("MAILME_TEMPLATE", defCmd).replace("$$BODY$$", body).replace("$$SUBJECT$$", subject)

    Bash.eval(mailCmd)
  }


  def isInPath(tool: String) = Bash.eval(s"which $tool").sout.trim.nonEmpty


  def requireInPath(tool: String) = require(isInPath(tool), s"$tool is not in PATH")
}
