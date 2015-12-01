import java.io.{BufferedReader, PrintStream}

import scala.sys.process.{ProcessIO, _}
import scalautils.Bash._

eval("ls -la")

var err = ""
var out = ""

def mkMonitor(allReader: BufferedReader, monitorStream: PrintStream = Console.out) = {
  // adopted from scala.io.BufferedSource.mkString
  val sb = new StringBuilder
  val buf = new Array[Char](2048)
  var n = 0

  while (n != -1) {
    n = allReader.read(buf)
    monitorStream.print(buf)
    if (n > 0) sb.appendAll(buf, 0, n)
  }

  sb.result
}

val io = new ProcessIO(
  stdin => None,
  stdout => {
    out = mkMonitor(scala.io.Source.fromInputStream(stdout).bufferedReader())
    stdout.close()
  },
  stderr => {
    err = scala.io.Source.fromInputStream(stderr).mkString
    stderr.close()
  })


val script = """ for t in $(seq 1 5); do sleep 1; echo $t; done """

Seq("/bin/bash", "-c", s"$script").run(io).exitValue()
