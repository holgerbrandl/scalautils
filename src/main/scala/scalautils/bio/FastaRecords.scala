package scalautils.bio

import better.files.File

import scalautils.FastaUtils._

/**
  * A collection of fasta records along with various tools to manipulate them
  *
  * @author Holger Brandl
  */
class FastaRecords(seqIt: Iterator[FastaRecord]) {

  // tbd how to prevent that the iterator progresses when using it

  def write(outputFile: File) = writeFasta(seqIt.toIterable, outputFile)


  def shuffle: FastaRecords = ???


  def letterStats = ???


  def createChunks(namingScheme: ChunkNamer) = ???
}

object FastaRecords {

  def fromFile(fastaFile: File) = openFasta(fastaFile)


  def fromChunks(fastaFiles: Seq[File]) = ???
}
