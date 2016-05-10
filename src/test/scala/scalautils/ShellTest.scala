package scalautils

import better.files.File._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scalautils.StringUtils.ImplStringUtils
import scalautils.r._


class ShellTest extends FlatSpec with Matchers with BeforeAndAfter {

  val wd = (home / ".scalautils_test" / "r").createIfNotExists(asDirectory = true)


  before {
    // fixme wont work with links currently, see  https://github.com/pathikrit/better-files/pull/72
    //    wd.glob("**").foreach(_.delete())

    wd.list.foreach(_.delete(true))
  }

  it should "rendr a small r markdown report from a snippt " in {

    rendrSnippet("test_report", """
    #' # Test Report

    require(ggplot2)


    #+ fig.width=10
    ggplot(iris, aes(Species)) + geom_bar()

    #' ## another section

    head(iris)

    #' bla bla bla

    1+1
    """.alignLeft, wd = wd)

    (wd / "test_report.html").toJava should exist
    (wd / "test_report.html").contentAsString should not be empty
  }

}
