import java.io.File

import scala.io.Source

/**
  * Document me!
  *
  * @author Holger Brandl
  */
package object BioUtils {


  case class FastaRecord(id: String, sequence: String)


  /** From http://codeaffectionate.blogspot.de/2013/05/reading-fasta-files-with-scala.html */
  def readFasta(filename: File): Seq[FastaRecord] = {
    fastaIterator(filename).toSeq.map(curSeq => FastaRecord(curSeq._1, curSeq._2.mkString))
  }


  def fastaIterator(filename: File) =
    new Iterator[(String, Iterator[Char])] {
      val header = """>(.+)(\|.+)?""".r
      var lines = Source.fromFile(filename).getLines().filterNot(_.isEmpty())


      def hasNext = lines.hasNext


      def next = {
        val header(name, annotation) = lines.next
        val (seq, rest) = lines.span(_ (0) != '>')
        lines = rest
        (name, seq.flatMap(_.iterator))
      }
    }


  // write fasta
  // see http://stackoverflow.com/questions/10530102/java-parse-string-and-add-line-break-every-100-characters
}
