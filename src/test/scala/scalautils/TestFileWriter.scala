package scalautils

import scalautils.IOUtils.saveAs

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object TestFileWriter extends App {

  import java.io.File

  val data = Array("Five", "stings", 3, 2)

  saveAs(new File("test.txt")) { p => {
    data.foreach(p.println)
  }
  }
}
