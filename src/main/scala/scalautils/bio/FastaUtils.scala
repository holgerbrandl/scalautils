package scalautils

import java.io.{BufferedReader, File => JFile, FileReader}

import better.files.File


/**
  * Document me!
  *
  * @author Holger Brandl
  */
package object FastaUtils {


  case class FastaRecord(id: String, description: Option[String] = None, sequence: String) {

    // inspired from see https://github.com/agjacome/funpep
    lazy val toEntryString: String = ">" + id + " " + description.getOrElse("") + "\n" + sequence.grouped(70).mkString("\n")

    // also interesting
    // see http://stackoverflow.com/questions/10530102/java-parse-string-and-add-line-break-every-100-characters
  }


  trait ChunkNamer {

    def getNext: File
  }


  class SimpleChunkNamer(baseDir: File = File("fasta_chunks"), prefix: String = "chunk_") extends ChunkNamer {

    var chunkCounter = 0


    def getNext: File = {
      chunkCounter = chunkCounter + 1
      baseDir / (prefix + chunkCounter + ".fasta")
    }
  }


  /** Inspired by http://stackoverflow.com/questions/7459174/split-list-into-multiple-lists-with-fixed-number-of-elements. */
  def createChunks(fastaIt: Iterator[FastaRecord], chunkSize: Int, namer: ChunkNamer = new SimpleChunkNamer()) = {

    fastaIt.grouped(chunkSize).foreach(chunk => {
      val nextChunkFile = namer.getNext
      if (!nextChunkFile.parent.exists) nextChunkFile.parent.createDirectory()

      assume(!nextChunkFile.exists) // make sure that we do not override existing chunk files

      IOUtils.saveAs(nextChunkFile.toJava) { p => chunk.foreach(record => p.append(record.toEntryString)) }
    })
  }

  /** From http://codeaffectionate.blogspot.de/2013/05/reading-fasta-files-with-scala.html */
  def readFasta(fastaFile: File): Seq[FastaRecord] = {
    openFasta(fastaFile).toSeq
  }


  def openFasta(fastaFile: File): Iterator[FastaRecord] = {
    new FastaReader(fastaFile.fullPath)
  }


  private class FastaReader(val filename: String) extends Iterator[FastaRecord] {

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

          return FastaRecord(id, Some(desc), sequencelist)
        }
      } while (reader.ready)
      // should never reach this...
      throw new FastaReadException("Error in file " + filename + " (tag=" + tag + ")")
    }
  }
}


/** From http://codeaffectionate.blogspot.de/2013/05/reading-fasta-files-with-scala.html */
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
