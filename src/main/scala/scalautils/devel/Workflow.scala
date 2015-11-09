package scalautils.devel

import better.files.File

/**
  * Document me!
  *
  * @author Holger Brandl
  */
object Workflow extends App {


  case class WorkingDirectory(dir: File) extends AnyVal


  //  implicit val cwd = WorkingDirectory(File("lala"))


  abstract case class Task(implicit val wd: WorkingDirectory = new WorkingDirectory(File("."))) {

    def cmd: String


    def change2wd = s"cd ${wd.dir.path};\n"
  }


  class Blast(query: File, subject: String, db: File = null) extends Task {

    //    override def cmd: String = s"blastn $query $subject"

    override def cmd: String = change2wd + "echo blastn is cool"
  }


  //  val blastn = new Blast(File("test.fasta"), "hsap")
  //  blastn.cmd
  //  blastn.wd


  Seq("home", "root", "somewhere").map(path => {
    // this cant' work because it's dynamic scoping (which implicents don't allow for since they are statically resolved (best explanation see http://www.scala-lang.org/old/node/2913)
    // same as in R where the env comes from the place where a method is defined

    implicit val newwd = WorkingDirectory(File(path))

    // note: it would work if Task and blast would be defined here
    new Blast(File("ll"), "sub").cmd
  }).foreach(println)


  //  Bash.eval()
  //  blas
}


