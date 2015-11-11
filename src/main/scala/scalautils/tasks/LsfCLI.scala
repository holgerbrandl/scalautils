package scalautils.tasks

/**
  * A command line interface to it
  *
  * @author Holger Brandl
  */
class LsfCLI extends App {

  if (args.length == 0) {
    Console.err.println("Usage: lsf_cli [task] [args]*")
    System.exit(-1)

    // todo    print bash function definitions here
  }


}
