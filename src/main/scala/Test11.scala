import java.io.InputStream

object Test11 extends App {
  val is2 :InputStream = getClass().getResourceAsStream("/home/fjbanez/Desktop/cursoSparkOderski/final/observatory/src/main/resources/1976.csv")


  val lines2: Iterator[String] = scala.io.Source.fromInputStream(is2).getLines()


  while (lines2.hasNext) {

    val linea2 = lines2.next()
    val trozos2: List[String] = linea2.split(",").toList

    println(trozos2)

  }


}
