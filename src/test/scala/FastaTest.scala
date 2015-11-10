import better.files.{File, _}

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object FastaTest extends App {

  private val testWD = home / "test"
  File("/Volumes/projects/plantx/on_plantx/dd_Jani_v4/dd_Jani_v4.dircor.fasta").copyTo(testWD)


}
