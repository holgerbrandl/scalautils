package scalautils.bio

import better.files.File

import scalautils.FastaUtils._

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object BlastUtils {

  /**
    * Given a fasta file and a blast result file, estimate the completeness of the blast-results.
    * This is e.g. helpful when checking for failed chunks in parallized hpc-setups.
    */
  def calculateProgress(fastaFile: File, blastOutput: File): Double = {
    require(fastaFile.isRegularFile && !fastaFile.isEmpty)
    require(blastOutput.isRegularFile)

    if (blastOutput.isEmpty) {
      return 0
    }

    val curProcessQuery = io.Source.fromFile(blastOutput.toJava).getLines.map(_.split("\t")(0)).toList.last

    val allSeqIds = readFasta(fastaFile).map(_.id)

    // find the position of the last results' id  in the fasta file
    val curIndex = allSeqIds.indexOf(curProcessQuery)

    curIndex / allSeqIds.length
  }
}
