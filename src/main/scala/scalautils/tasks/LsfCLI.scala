package scalautils.tasks

/**
  * A command line interface to it
  *
  * @author Holger Brandl
  */
object LsfCLI extends App {

  if (args.length == 0) {
    Console.err.println("Usage: lsf_cli [task] [args]*")
    System.exit(-1)

    // todo    print bash function definitions here
  }

  //  How to run?
  // sbt assembly
  // java -cp /Users/brandl/Dropbox/cluster_sync/scalautils/target/scala-2.11/scalautils-assembly-0.1-SNAPSHOT.jar scalautils.tasks.LsfCLI

}
