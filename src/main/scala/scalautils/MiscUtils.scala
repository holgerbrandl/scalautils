package scalautils


/**
  * Document me!
  *
  * @author Holger Brandl
  */
package object MiscUtils {

  // see http://stackoverflow.com/questions/4604237/how-to-write-to-a-file-in-scala
  //  http://stackoverflow.com/questions/6879427/scala-write-string-to-file-in-one-statement
  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try {
      op(p)
    } finally {
      p.close()
    }
  }

}
