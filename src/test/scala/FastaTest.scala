import better.files.File
import org.scalatest.{FlatSpec, Matchers}

import scalautils.FastaUtils._

class FastaTest extends FlatSpec with Matchers {

  // see http://www.scalatest.org/user_guide/using_matchers#greaterAndLessThan

  it should "create some fasta chunks from a source fasta " in {
    //  import better.files._, Cmds._

    val wd = File("/Volumes/home/brandl/test/")
    //   val sourceExample = File("/projects/plantx/on_plantx/dd_Jani_v4/dd_Jani_v4.dircor.fasta")
    //   val testFasta = sourceExample.copyTo(wd/"some_seqs.fasta")
    val testFasta = wd / "some_seqs.fasta"


    val fastaRecords = readFasta(testFasta)

    createChunks(openFasta(testFasta), 20, new SimpleChunkNamer(baseDir = wd, prefix = "test_chunk_"))


    wd.glob("**/test_chunk*").size should be > 0
  }

  it should "do something else " in {
    val tt = 1 + 1
  }
}
