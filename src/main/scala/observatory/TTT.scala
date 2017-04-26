package observatory

import java.nio.file.Paths



/**
  * Created by fjbanez on 26/04/17.
  */


object TTT extends App {

def fsPath(resource: String): String =
  Paths.get(getClass.getResource(resource).toURI).toString

println(fsPath("/prueba/stations.csv"))

  /*val is: InputStream = getClass.getResourceAsStream("1977.csv")
  val lines2: Iterator[String] = scala.io.Source.fromInputStream(is).getLines()
  while (lines2.hasNext) {

    //println(lines2.next())
  }*/
}
