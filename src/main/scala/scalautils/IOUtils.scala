package scalautils

import java.io.File

import scala.language.implicitConversions


/**
  * Document me!
  *
  * @author Holger Brandl
  */
object IOUtils {

  // for partially applied functions see
  // http://stackoverflow.com/questions/14309501/scala-currying-vs-partially-applied-functions
  def saveAs(fileName: String): Unit = saveAs(new File(fileName)) _


  def saveAs(file: better.files.File): Unit = saveAs(file.toJava) _

  // see http://stackoverflow.com/questions/4604237/how-to-write-to-a-file-in-scala
  //  http://stackoverflow.com/questions/6879427/scala-write-string-to-file-in-one-statement
  // similar http://jesseeichar.github.io/scala-io-doc/0.2.0/index.html#!/core/multiple_writes_single_connection

  def saveAs(f: java.io.File)(op: java.io.PrintWriter => Unit, overwrite: Boolean = false) {
    if (f.isFile && !overwrite) throw new IllegalArgumentException(s"$f is present already")
    val p = new java.io.PrintWriter(f)
    try {
      op(p)
    } finally {
      p.close()
    }
  }


  def saveAs(f: File, lines: Seq[String]): Unit = saveAs(f) { p => lines.foreach(p.println) }


  /** Also consider sbt solution
    val lines: Vector[String] = IO.readLines(f).toVector

    // Writing
    IO.writeLines(file1, lines)

    */


  /** implicit conversion rules. To use them do   import de.mpicbg.rink.plantx.FileUtils._ */
  @Deprecated
  object FileUtils {


    implicit class FileApiImplicits(file: File) {

      @Deprecated
      def mkdirOptional = {
        if (!file.isDirectory) file.mkdir()
        file
      }
    }


    @Deprecated
    implicit def string2file(s: String): File = new File(s)


    @Deprecated
    implicit def file2string(s: String): String = s.getAbsolutePath

  }


  /** implicit conversion rules. To use them do   import de.mpicbg.rink.plantx.FileUtils._ */
  @Deprecated
  object BetterFileUtils {


    implicit class FileApiImplicits(file: better.files.File) {

      @Deprecated
      def mkdirOptional = {
        if (!file.isDirectory) file.createDirectory()
        file
      }
    }


    @Deprecated
    implicit def string2file(s: String): File = new File(s)


    @Deprecated
    implicit def file2string(s: String): String = s.getAbsolutePath

  }


}


