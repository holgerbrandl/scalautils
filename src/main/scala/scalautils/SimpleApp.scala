package scalautils

/**
 * Document me!
 *
 * @author Holger Brandl
 */
// note: The App trait can be used to quickly turn objects into executable programs. Here is an example:
// "object" serves the same (and some additional) purposes as the static methods and fields in Java
object SimpleApp extends App{
  println("test")

  def doSomething = println("something")
}
