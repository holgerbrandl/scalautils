package scalautils

import better.files.File

/**
  * @author Holger Brandl
  */
package object r {

  /**
    *
    * @return The create html report file
    */
  def rendrSnippet(reportName: String, rSnippet: String,
                   showCode: Boolean = true, args: String = "", wd: File = File(".")) = {

    val reportFileName = reportName.replaceAll("[/ ]", "_")
    require(Bash.eval("which rend.R").stdout.nonEmpty, "rend.R is not installed. See https://github.com/holgerbrandl/datautils/tree/master/R/rendr")

    val tempDir = File.newTempDir(prefix = "rsnip_")

    val tmpR = tempDir / (reportFileName + ".R")

    tmpR.write(rSnippet)

    Bash.eval(s"""cd "${wd.path}"; rend.R ${if (showCode) "-E" else ""} "${tmpR.path}" $args""")

    // todo convert result into status code
    wd / (reportFileName + ".html")
  }
}
