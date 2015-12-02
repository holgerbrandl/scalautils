package scalautils

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * @author Holger Brandl
  */
class MiscUtils {

  class ThreadingUtils() {

    implicit class awaitFuture[A](fut: Future[A]) {
      def await() = Await.result(fut, Duration.Inf)
    }

  }

}
