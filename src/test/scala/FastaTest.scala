import better.files.File
import org.scalatest.{FlatSpec, Matchers}

/**
  * Document me!
  *
  * @author Holger Brandl
  */
class FastaTest extends FlatSpec with Matchers {

  it should "create some fasta chunks from a source fasta " in {
    //  import better.files._, Cmds._

    val testWD = File("/Volumes/home/brandl/test/")
    //   val sourceExample = File("/projects/plantx/on_plantx/dd_Jani_v4/dd_Jani_v4.dircor.fasta")
    //   val testFasta = sourceExample.copyTo(testWD/"some_seqs.fasta")
    val testFasta = testWD / "some_seqs.fasta"


    //  "cp /projects/plantx/on_plantx/dd_Jani_v4/dd_Jani_v4.dircor.fasta /Volumes/home/brandl/test/"
    //  cp(sourceExample, testWD)

    testWD.list

    import scalautils.FastaUtils._

    val fastaRecords = readFasta(testFasta)

    createChunks(openFasta(testFasta), 20, new SimpleChunkNamer(baseDir = testWD, prefix = "test_chunk_"))


    testWD.glob("**/test_chunk*").size should be > 0
  }

  it should "do something else " in {
    val tt = 1 + 1
  }
}
