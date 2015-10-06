package scalautils

/**
 * Document me!
 * @author Holger Brandl
 */
// note: The App trait can be used to quickly turn objects into executable programs. Here is an example:

// http://stackoverflow.com/questions/1755345/difference-between-object-and-class-in-scala
// "object" serves the same (and some additional) purposes as the static methods and fields in Java
// Scala doesn't have static methods or fields. Instead you should use object

object SimpleApp extends App{

  println("initialize me!")


  def doSomething = println("something else")
}
