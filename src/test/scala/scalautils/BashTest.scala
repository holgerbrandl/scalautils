package scalautils

/**
  * @author Holger Brandl
  */
object BashTest extends App {

  //  import scalautils.Bash
  Bash.eval("""cd ~/unit_tests
  echo baum > baum.txt
  echo haus > haus.txt""")
  Bash.eval("cd ~/unit_tests\n echo baum > baum2.txt\necho haus > haus2.txt")


  val jobCmed = "cd '/home/brandl/unit_tests'; mysub \"Users__brandl__633224592__1112201512d4102\" 'cd /home/brandl/unit_tests ## lsfutils:change into wd\nsleep 60; echo \"this is task 1\" > task_1.txt ' -q short   | joblist /home/brandl/unit_tests/.test_tasks"
  Bash.eval(jobCmed)
}
