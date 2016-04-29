package scalautils

/**
  * @author Holger Brandl
  */
package object math {

  // http://stackoverflow.com/questions/4753629/how-do-i-make-a-class-generic-for-all-numeric-types
  // http://stackoverflow.com/questions/3498784/scala-calculate-average-of-someobj-double-in-a-listsomeobj/34196631#34196631
  implicit class ImplDoubleVecUtils(values: Seq[Double]) {

    def mean = values.sum / values.length


    // http://stackoverflow.com/questions/4662292/scala-median-implementation
    def median = {
      val (lower, upper) = values.sorted.splitAt(values.size / 2)
      if (values.size % 2 == 0) (lower.last + upper.head) / 2.0 else upper.head
    }

    def quantile(quantile:Double) = {
      assert(quantile >=0 && quantile <=1)
      // convert quantile into and index
      val quantIndex = (values.length.toDouble*quantile).round.toInt -1
      values.sorted.get(quantIndex)
    }
  }

}
