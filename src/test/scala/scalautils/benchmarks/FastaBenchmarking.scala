package scalautils.benchmarks

import java.text.DecimalFormat

import better.files._

import scalautils.FastaUtils._
import scalautils.IOUtils.BetterFileUtils.FileApiImplicits
import scalautils.bio.FastaRecords
import scalautils.bio.fasta.ImplFastaTools

object FastaBenchmarking extends App {

  def time[A](msg: String = "time")(f: => A) = {
    val s = System.nanoTime
    val ret = f
    Console.err.println(msg + ": " + new DecimalFormat("#0.##").format((System.nanoTime - s) / 1e9) + "sec")
    ret
  }


  private val testFasta = File("/Users/brandl/Desktop/dd_Bcan_v1.fasta")
  val fastaRecords = FastaRecords.fromFile(testFasta)

  time("get size") {
    Console.err.println(s"num records ${fastaRecords.size}")
  }

  time("write_fasta") {
    fastaRecords.write(File.newTemp("fastaTest").delete())
  }


  time("write_fasta_again") {
    fastaRecords.write(File.newTemp("fastaTest").delete())
  }

  time("shuffle_fasta") {
    val shuffledFasta = fastaRecords.shuffle
    shuffledFasta.write(testFasta.withExt(".shuffled.fasta").delete(true))
  }

  val shuffledFasta = fastaRecords.shuffle

  time("create_chunks") {
    val chunkSize = 6000
    val chunkLabeler = new SimpleChunkNamer(prefix = "fasta_chunk_", baseDir = (testFasta.parent / "protein_chunks").delete(true))
    val chunks = shuffledFasta.createChunks(chunkSize, chunkLabeler)
  }
}
