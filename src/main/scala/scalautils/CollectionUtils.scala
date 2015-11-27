package scalautils



/**
  * @author Holger Brandl
  */
object CollectionUtils {

  implicit class StrictSetOps[T](someSeq: Seq[T]) {

    def strictDiff(that: Seq[T]) = {
      someSeq.diff(that).seq
    }


    def strictUnion(that: Seq[T]) = {
      someSeq.union(that)
    }


    def strictIntersect(that: Seq[T]) = {
      someSeq.intersect(that)
    }
  }

}
