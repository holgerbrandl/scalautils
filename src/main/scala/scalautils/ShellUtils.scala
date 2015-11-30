package scalautils

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object ShellUtils {

  def mailme(subject: String, body: String = "", logSubject: Boolean = true) = {
    if (logSubject) Console.err.println(s"$subject")

    Bash.eval(s"""echo -e 'Subject:$subject\n\n $body' | sendmail $$(whoami)@mpi-cbg.de > /dev/null""")
  }


  def isInPath(tool: String) = Bash.eval(s"which $tool").sout.isEmpty


  def requireInPath(tool: String) = require(isInPath(tool), s"$tool is not in PATH")
}
