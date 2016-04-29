package scalautils

/**
  * Document me!
  *
  * @author Holger Brandl
  */


/** implicit string utilities. To use them import de.mpicbg.rink.plantx.StringUtils._ */
//https://www.safaribooksonline.com/library/view/scala-cookbook/9781449340292/ch01s11.html
object StringUtils {


  implicit class ImplStringUtils(s: String) {

    ///http://stackoverflow.com/a/6061104/590437
    def stripLeadingWS = s.split("\n").map(_.trim).mkString("\n").trim


    def println() = Console.println(s)


    def alignLeft = {
      val split = s.split("\n")
      val minWS = split.filter(_.trim.length > 0).map(line => {
        line.split("\\S").headOption.getOrElse("").length
        // or use lift(3) see http://stackoverflow.com/questions/4981689/get-item-in-the-list-in-scala
      }).min
//        http://stackoverflow.com/questions/10922237/scala-min-max-with-optiont-for-possibly-empty-seq
//        sorted.headOption.getOrElse(0)

      split.map(line => if (line.trim.isEmpty) "" else line.substring(minWS)).mkString("\n")
    }
  }
}

//object Test extends App{
//  import StringUtils._
//  Seq("", "   test").mkString("\n").alignLeft.mkString.println
//
//}