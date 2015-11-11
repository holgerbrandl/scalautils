package scalautils

import java.io.File

import scala.language.implicitConversions


/**
  * Document me!
  *
  * @author Holger Brandl
  */
object IOUtils {

  def saveAs(fileName: String) = saveAs(new File(fileName))


  def saveAs(file: better.files.File) = saveAs(file.toJava)

  // see http://stackoverflow.com/questions/4604237/how-to-write-to-a-file-in-scala
  //  http://stackoverflow.com/questions/6879427/scala-write-string-to-file-in-one-statement
  // similar http://jesseeichar.github.io/scala-io-doc/0.2.0/index.html#!/core/multiple_writes_single_connection

  def saveAs(f: java.io.File)(op: java.io.PrintWriter => Unit) {
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
  object FileUtils {


    implicit class FileApiImplicits(file: File) {

      def mkdirOptional = {
        if (!file.isDirectory) file.mkdir()
        file
      }
    }


    implicit def string2file(s: String): File = new File(s)


    implicit def file2string(s: String): String = s.getAbsolutePath

  }


  /** implicit conversion rules. To use them do   import de.mpicbg.rink.plantx.FileUtils._ */
  object BetterFileUtils {


    implicit class FileApiImplicits(file: better.files.File) {

      def mkdirOptional = {
        if (!file.isDirectory) file.createDirectory()
        file
      }
    }


    implicit def string2file(s: String): File = new File(s)


    implicit def file2string(s: String): String = s.getAbsolutePath

  }


}


