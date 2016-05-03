package scalautils

import better.files.File

import scala.io.Source

/**
  * @author Holger Brandl
  */
package object r {

  //todo port back into scalautils
  def rendrSnippet(reportName: String, rSnippet: String,
                   showCode: Boolean = true, args: String = "", wd: File = File(".")) = {


    val reportFileName = reportName.replaceAll("[/ ]", "_")
    require(ShellUtils.isInPath("R"), "Can not render report because R is not available on your system.")

    //    require(ShellUtils.isInPath("rend.R"), "rend.R is not installed. See https://github.com/holgerbrandl/datautils/tree/master/R/rendr")

    // bootstrap rend.R if it is not yet installed
    val rendr2use = if(!ShellUtils.isInPath("rend.R")){
      val rendR: File = File.home/".jl_rend.R"

      if(!rendR.exists) {
        val rendrURL: String = "https://raw.githubusercontent.com/holgerbrandl/datautils/v1.23/R/rendr/rend.R"
        IOUtils.saveAs(rendR.toJava, Source.fromURL(rendrURL).getLines().toSeq)
        rendR.toJava.setExecutable(true)
      }

      rendR.path.toAbsolutePath
    }else{
      "rend.R"
    }

    val tempDir = File.newTemporaryDirectory(prefix = "rsnip_")

    val tmpR = tempDir / (reportFileName + ".R")

    tmpR.write(rSnippet)

    Bash.eval(s"""cd "${wd.path}"; ${rendr2use} ${if (showCode) "" else "-e"} "${tmpR.path}" $args""")

    // todo convert result into status code
    wd / (reportFileName + ".html")
  }
}
