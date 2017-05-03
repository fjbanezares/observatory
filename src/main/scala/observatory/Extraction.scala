package observatory

import java.time.LocalDate

import java.io.InputStream
import java.nio.file.Paths



import scala.collection.mutable.ListBuffer


/**
  * 1st milestone: data extraction
  */
object  Extraction {

  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString




  type STNWBAN =  (String,String)

  type coordenadas = (Double,Double)

  //(LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
  // var listaResultante: List[LocalDate, Location]









  def farenheitToCelsios(temperature: Double):Double = ((temperature - 32) * 5 / 9)



  final case class Body(STN: String,
                        WBAN: String,
                        month: Int,
                        day: Int,
                        avgTemperature: Double,
                        latitud: Double,
                        longitud: Double)
  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    *
    *         TermperaturesFile : (010010,_,01,01,7.5) --> 1 de enero, 7.5 grados de media
    *         De un fichero y las estaciones nos da
    *
    *         stationsFile: (010860,_,+70.600,+029.693)
    *
    *         case class Location(lat: Double, lon: Double)
    *
    *
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {

    //creo mapa de estaciones en memoria
    var mapita = scala.collection.mutable.Map[STNWBAN, coordenadas]()
    val is: InputStream = getClass.getResourceAsStream(stationsFile)
    val lines: Iterator[String] = scala.io.Source.fromInputStream(is).getLines()
    while (lines.hasNext) {

      val linea = lines.next()
      val trozos: List[String] = linea.split(",").toList
      if (trozos.size == 4) mapita((trozos(0), trozos(1))) = (trozos(2).toDouble, trozos(3).toDouble)
    }

    val is2: InputStream = getClass.getResourceAsStream(temperaturesFile)
    val lines2: Iterator[String] = scala.io.Source.fromInputStream(is2).getLines()

    var fruits = new ListBuffer[(LocalDate, Location, Double)]()

    while (lines2.hasNext) {

      val linea2 = lines2.next()
      val trozos2: List[String] = linea2.split(",").toList
      if (trozos2.size == 5 && mapita.contains((trozos2(0), trozos2(1)))) {
        val location: coordenadas = mapita((trozos2(0), trozos2(1)))
        val pepito = (LocalDate.of(year, trozos2(2).toInt, trozos2(3).toInt), Location(location._1, location._2), farenheitToCelsios(trozos2(4).toDouble))
        fruits += pepito
      }

      //
    }
    fruits
  }


  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] =
    records.map(x=> (x._2,x._3)).groupBy(x=>x._1).map(x=>(x._1,x._2.map(x => x._2))).map(x=>(x._1,x._2.sum/x._2.size)).toSeq

}
