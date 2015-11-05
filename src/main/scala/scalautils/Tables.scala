package scalautils

import java.io.File

import scala.io.Source

/**
  * Document me!
  *
  * @author Holger Brandl
  */


object Tables {


  case class DataFrame(header: Array[String], data: Seq[Array[String]])


  def readTSV(file: File): DataFrame = {
    val data = Seq(Source.fromFile(file).
      getLines().map(_.replace("\"", "")).
      map(_.split('\t')).
      toSeq: _*)

    DataFrame(data.head, data.drop(1))

  }
}
