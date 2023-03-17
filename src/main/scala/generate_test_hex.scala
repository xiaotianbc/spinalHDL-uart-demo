import java.io.{File, PrintWriter}

class generate_test_hex {
}

object generate_test_hex extends App {
  val s = "nice to meet you, SpinalHDL!"
  val sa = s.map(_.toInt.toHexString)

  val printWriter_name = new PrintWriter(new File("output.hex"))
  for (i <- sa){
    printWriter_name.write(i)
    printWriter_name.write("\n")
  }
  printWriter_name.close
}