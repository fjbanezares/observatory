package observatory

import java.io.InputStream
import java.nio.file.Paths

import observatory.Extraction.getClass


/**
  * Created by Pedro on 30/04/2017.
  */
object TestSobreGeneracionPrimerMapaSemana2 extends App{
  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString

 // println(fsPath("/prueba/1977.csv"))

// val is: InputStream = getClass.getResourceAsStream("/prueba/1977.csv")
  //val lines: Iterator[String] = scala.io.Source.fromInputStream(is).getLines()
// println(lines.next())

/*  Visualization.visualize(Extraction.locationYearlyAverageRecords(
    Extraction.locateTemperatures(1977,"/prueba/stations.csv","/prueba/1977.csv")),Visualization.ejemploColors).output(new java.io.File("target/primermapita.png"))*/

Interaction.tile(Extraction.locationYearlyAverageRecords(
  Extraction.locateTemperatures(1977,"/prueba/stations.csv","/prueba/1977.csv")),Visualization.ejemploColors,0,0,0).output(new java.io.File("target/primermapita.png"))
}
