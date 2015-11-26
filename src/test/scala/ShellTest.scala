import better.files._
import org.scalatest.{FlatSpec, Matchers}

import scalautils.StringUtils.ImplStringUtils
import scalautils.r._

class ShellTest extends FlatSpec with Matchers {

  val wd = (home / ".scalautils_test" / "r").createIfNotExists(true)


  it should "rendr a small r markdown report from a snippt " in {

    // clean up previous chunks
    wd.glob("**").foreach(_.delete())
    wd.createIfNotExists(true) //todo glob ** should not capture the wd itself (b.f. bug?)

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
