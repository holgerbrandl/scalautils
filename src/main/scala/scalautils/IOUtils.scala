package scalautils

import java.io.{File, PrintWriter}

import scala.language.{implicitConversions, postfixOps}
import scala.sys.process._


/**
  * Document me!
  *
  * @author Holger Brandl
  */
object IOUtils {

  // for partially applied functions see
  // http://stackoverflow.com/questions/14309501/scala-currying-vs-partially-applied-functions
  def saveAs(fileName: String): ((PrintWriter) => Unit) => Unit = saveAs(new File(fileName))


  def saveAs(file: better.files.File): ((PrintWriter) => Unit) => Unit = saveAs(file.toJava)

  // see http://stackoverflow.com/questions/4604237/how-to-write-to-a-file-in-scala
  //  http://stackoverflow.com/questions/6879427/scala-write-string-to-file-in-one-statement
  // similar http://jesseeichar.github.io/scala-io-doc/0.2.0/index.html#!/core/multiple_writes_single_connection

  def saveAs(f: java.io.File, overwrite: Boolean = false)(op: java.io.PrintWriter => Unit) {
    if (f.isFile && !overwrite) throw new IllegalArgumentException(s"$f is present already")
    val p = new java.io.PrintWriter(f)
    try {
      op(p)
    } finally {
      p.close()
    }
  }


  def saveAs(f: File, lines: Seq[String]): Unit = saveAs(f) { p => lines.foreach(p.println) }


  /** implicit conversion rules. To use them do   import de.mpicbg.rink.plantx.FileUtils._ */
  @Deprecated
  object BetterFileUtils {


    implicit class FileApiImplicits(file: better.files.File) {

      def head = s"head ${file}" !


      def withExt(extension: String) = better.files.File(file + extension)
    }
  }
}


