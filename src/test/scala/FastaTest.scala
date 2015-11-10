import better.files._
import org.scalatest.{FlatSpec, Matchers}

import scalautils.FastaUtils._

class FastaTest extends FlatSpec with Matchers {

  // see http://www.scalatest.org/user_guide/using_matchers#greaterAndLessThan

  it should "create some fasta chunks from a source fasta " in {


    val wd = (home / "unit_tests").createIfNotExists(true)

    // clean up previous chunks
    wd.glob("**/test_chunk*").foreach(_.delete())

    // val testFasta = File("/projects/plantx/on_plantx/dd_Jani_v4/dd_Jani_v4.dircor.fasta").copyTo(wd/"some_seqs.fasta")
    val testFasta = File(getClass.getResource("/bio/some_seqs.fasta").getFile) //http://stackoverflow.com/questions/5285898/how-to-access-test-resources
    //    val testFasta = wd / "some_seqs.fasta"


    //    val fastaRecords = readFasta(testFasta)
    val chunks = createChunks(openFasta(testFasta), 50, new SimpleChunkNamer(baseDir = wd, prefix = "test_chunk_"))

    wd.glob("**/test_chunk*").size should be > 0

    // ensure that the chunks actually contain 20 sequences
    val chunkRecords = readFasta(wd.glob("**/test_chunk*").next())
    chunkRecords should have size 50

    // validate a single record
    val firstRecord = chunkRecords.head
    firstRecord.description shouldEqual None
    firstRecord.id should startWith("dd_")
  }


  it should "do something else " in {
    val tt = 1 + 1
  }
}
