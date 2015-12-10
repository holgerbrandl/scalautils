package scalautils.bio

import better.files.File

import scala.util.Random
import scalautils.FastaUtils
import scalautils.FastaUtils._

/**
  * A collection of fasta records along with various tools to manipulate them
  *
  * @author Holger Brandl
  */

object fasta {
  // http://stackoverflow.com/questions/11302270/what-is-the-relation-between-iterable-and-iterator
  type FastaRecords = Iterable[FastaRecord]

  implicit class ImplFastaTools(records: FastaRecords) {


    // how to prevent that the iterator progresses when using it --> use iterable

    // http://stackoverflow.com/questions/19804928/scala-writing-string-iterator-to-file-in-efficient-way
    def write(outputFile: File) = writeFasta(records, outputFile)


    def shuffle: FastaRecords = Random.shuffle(records)


    //    http://stackoverflow.com/questions/12105130/generating-a-frequency-map-for-a-string-in-scala
    def letterStats = {
      records.map(_.sequence).mkString.groupBy(_.toChar).map(letter => letter._1 -> letter._2.length)
    }


    def gcContent = {
      val stats = letterStats
      (stats('G') + stats('C')) / stats.values.sum
    }


    def createChunks(chunkSize: Int, namingScheme: ChunkNamer) = {
      FastaUtils.createChunks(records, chunkSize = chunkSize, namer = namingScheme)
    }
  }

}

object FastaRecords {

  def fromFile(fastaFile: File) = openFasta(fastaFile)


  def fromChunks(fastaFiles: Seq[File]) = {
    fastaFiles.flatMap(FastaRecords.fromFile)
  }
}
