package scalautils

import java.io.{BufferedReader, File, FileReader}

/**
  * Document me!
  *
  * @author Holger Brandl
  */
package object bio {


  case class FastaRecord(id: String, description = Option[String], sequence: String)


  /** From http://codeaffectionate.blogspot.de/2013/05/reading-fasta-files-with-scala.html */
  def readFasta(filename: File): Seq[FastaRecord] = {
    new FastaReader(filename.getAbsolutePath).toSeq
  }


  //
  //
  //  def fastaIterator(filename: File) =
  //    new Iterator[(String, Iterator[Char])] {
  //      val header = """>(.+)(\|.+)?""".r
  //      var lines = Source.fromFile(filename).getLines().filterNot(_.isEmpty())
  //
  //
  //      def hasNext = lines.hasNext
  //
  //
  //      def next = {
  //        val header(name, annotation) = lines.next
  //        val (seq, rest) = lines.span(_ (0) != '>')
  //        lines = rest
  //        (name, seq.flatMap(_.iterator))
  //      }
  //    }


  class FastaReader(val filename: String) extends Iterator[FastaRecord] {

    private lazy val reader = new BufferedReader(new FileReader(filename))


    class FastaReadException(string: String) extends Exception(string)


    def hasNext() = reader.ready


    def next(): FastaRecord = {
      // Read the tag line
      val tag = reader.readLine
      if (tag(0) != '>')
        throw new FastaReadException("record start expected")
      var sequencelist = ""
      // Read the sequence body
      do {
        reader.mark(512) // 512 is sufficient for a single tag line
        val line = reader.readLine
        if (line(0) != '>') sequencelist += line
        if (!reader.ready || line(0) == '>') {
          // Reached the end of the sequence
          if (reader.ready) reader.reset()
          // Remove prepending '>'
          val desc = tag.drop(1).trim
          val id = desc.split(Array(' ', '\t'))(0)
          return FastaRecord(id, desc, sequencelist)
        }
      } while (reader.ready)
      // should never reach this...
      throw new FastaReadException("Error in file " + filename + " (tag=" + tag + ")")
    }
  }

  // write fasta
  // see http://stackoverflow.com/questions/10530102/java-parse-string-and-add-line-break-every-100-characters
}
