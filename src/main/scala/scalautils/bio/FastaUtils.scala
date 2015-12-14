package scalautils

import java.io.{File => JFile}

import better.files.File

import scala.collection.mutable.ListBuffer


/**
  * Document me!
  *
  * @author Holger Brandl
  */
package object FastaUtils {


  case class FastaRecord(id: String, description: Option[String] = None, sequence: String) {

    // inspired from see https://github.com/agjacome/funpep
    lazy val toEntryString: String = ">" + id + " " + description.getOrElse("") + "\n" + sequence.grouped(70).mkString("\n") + "\n"


    // also interesting
    // see http://stackoverflow.com/questions/10530102/java-parse-string-and-add-line-break-every-100-characters
  }


  trait ChunkNamer {

    def getNext: File
  }


  case class SimpleChunkNamer(baseDir: File = File("fasta_chunks"), prefix: String = "chunk_") extends ChunkNamer {


    var chunkCounter = 0


    def getNext: File = {
      chunkCounter = chunkCounter + 1
      baseDir / (prefix + chunkCounter + ".fasta")
    }


    /**
      * List existing chunks
      */
    def list = {
      val copyNamer = this.copy()
      var chunkFile = copyNamer.getNext

      var chunks = new ListBuffer[File]
      while (chunkFile.exists) {
        chunks += chunkFile
        chunkFile = copyNamer.getNext
      }

      chunks
    }
  }


  /** Inspired by http://stackoverflow.com/questions/7459174/split-list-into-multiple-lists-with-fixed-number-of-elements. */
  // todo use proper api instead of static method to create chunks
  def createChunks(fastaIt: Iterable[FastaRecord], chunkSize: Int, namer: ChunkNamer = new SimpleChunkNamer()) = {

    fastaIt.grouped(chunkSize).map(chunk => {
      val nextChunkFile = namer.getNext
      if (!nextChunkFile.parent.exists) nextChunkFile.parent.createDirectory()

      assume(!nextChunkFile.exists, s"$nextChunkFile is already present") // make sure that we do not override existing chunk files

      writeFasta(chunk, nextChunkFile)

      nextChunkFile
    }).toIndexedSeq //
  }


  def writeFasta(fastaRecords: Iterable[FastaRecord], outputFile: File): Unit = {
    IOUtils.saveAs(outputFile.toJava) { p => fastaRecords.foreach(record => p.append(record.toEntryString)) }
  }


  /** From http://codeaffectionate.blogspot.de/2013/05/reading-fasta-files-with-scala.html */
  def readFasta(fastaFile: File): Seq[FastaRecord] = {
    openFasta(fastaFile).toSeq
  }


  def openFasta(fastaFile: File) = {
    //    new FastaReader(fastaFile.pathAsString)
    new BufferedFastaReader(fastaFile).toIterable
  }


  private class BufferedFastaReader(val file: File) extends Iterator[FastaRecord] {

    private val fileIt = io.Source.fromFile(file.toJava).getLines().filterNot(_.isEmpty).buffered


    def hasNext() = fileIt.hasNext


    def next(): FastaRecord = {
      val tag = fileIt.next() // Read the tag line

      if (tag(0) != '>') throw new FastaReadException("record start expected")

      val sb: StringBuilder = new StringBuilder()

      do {
        sb.append(fileIt.next())
      } while (fileIt.hasNext && !fileIt.head.startsWith(">"))

      // Remove prepending '>' and separate description from id
      val header: Array[String] = tag.drop(1).trim.split("\\s+")
      val id = header(0)
      val desc = if (header.length == 2) Some(header.drop(1).mkString(" ")) else None

      // todo use string-builder directly in FastaRecord to speed up fasta-processing
      FastaRecord(id, desc, sb.toString())
    }
  }

}

//   class FastaReader(val filename: String) extends Iterator[FastaRecord] {
//
//    private lazy val reader = new BufferedReader(new FileReader(filename))
//
//
//    class FastaReadException(string: String) extends Exception(string)
//
//
//    def hasNext() = reader.ready
//
//
//    def next(): FastaRecord = {
//      // Read the tag line
//      val tag = reader.readLine
//      if (tag(0) != '>')
//        throw new FastaReadException("record start expected")
//      var sequencelist = ""
//      // Read the sequence body
//      do {
//        reader.mark(512) // 512 is sufficient for a single tag line
//        val line = reader.readLine
//        if (line(0) != '>') sequencelist += line
//        if (!reader.ready || line(0) == '>') {
//          // Reached the end of the sequence
//          if (reader.ready) reader.reset()
//
//          // Remove prepending '>' and separate header from id
//          val splitRecHeader = tag.drop(1).trim.split(Array(' ', '\t'))
//          val id = splitRecHeader(0)
//          val desc = if (splitRecHeader.length == 2) Some(splitRecHeader(1)) else None
//
//          return FastaRecord(id, desc, sequencelist)
//        }
//      } while (reader.ready)
//      // should never reach this...
//      throw new FastaReadException("Error in file " + filename + " (tag=" + tag + ")")
//    }
//  }


class FastaReadException(string: String) extends Exception(string)

/** From http://codeaffectionate.blogspot.de/2013/05/reading-fasta-files-with-scala.html */
//  def fastaIterator(filename: File) =
//    new Iterator[(String, Iterator[Char])] {
//      val header = """>(.+)(\|.+)?""".r
//      var lines = Source.fromFile(filename).getLines().filterNot(_.isEmpty())
//
//
//      def hasNext = lines.hasNext
//
//      def next = {
//        val header(name, annotation) = lines.next
//        val (seq, rest) = lines.span(_ (0) != '>')
//        lines = rest
//        (name, seq.flatMap(_.iterator))
//      }
//    }
