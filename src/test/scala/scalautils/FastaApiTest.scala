package scalautils

import better.files._

import scala.util.Random
import scalautils.FastaUtils._
import scalautils.IOUtils.BetterFileUtils.FileApiImplicits

object FastaApiTest extends App {

  val testFasta = File("/Users/brandl/Desktop/dd_Bcan_v1.fasta")
  //  val testFasta = File("/projects/plantx/inprogress/stowers/dd_Gsan_v4/dd_Gsan_v4.fasta")


  val fastaRecords: Seq[FastaRecord] = readFasta(testFasta)

  println(s"number of records is ${fastaRecords.size}")

  println(s"shuffling")
  val shuffledFasta = testFasta.withExt(".shuffled.fasta").delete(true)
  writeFasta(Random.shuffle(readFasta(testFasta)), shuffledFasta)

  // create chunks
  println(s"chunking")
  val chunkSize = 6000
  val chunkLabeler = new SimpleChunkNamer(prefix = "fasta_chunk_", baseDir = (testFasta.parent / "protein_chunks").delete(true))
  val chunks = createChunks(openFasta(shuffledFasta), chunkSize = chunkSize, namer = chunkLabeler)
  //val chunks = chunkLabeler.list // restore previously created chunk list


  println("chunk creation done")


  //    val chunks = createChunks(openFasta(testFasta), 200, new SimpleChunkNamer(baseDir = wd, prefix = "test_chunk_"))

  //    wd.glob("**/test_chunk*").size should be > 0
  //
  //    // ensure that the chunks actually contain 20 sequences
  //    val chunkRecords = readFasta(wd.glob("**/test_chunk*").next())
  //    chunkRecords should have size 200
  //
  //    // validate a single record
  //    val firstRecord = chunkRecords.head
  //    firstRecord.description shouldEqual None
  //    firstRecord.id should startWith("dd_")

}
