import java.nio.file.Paths
import java.io.InputStream
import java.lang.Class
import java.time.LocalDate
import java.util

import scala.collection.mutable.ListBuffer

object Test extends App {

  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString

  //println(fsPath("/stations.csv"))

  val is :InputStream = getClass.getResourceAsStream("/prueba/stations.csv")

  val lines: Iterator[String] = scala.io.Source.fromInputStream( is ).getLines()

  type STNWBAN =  (String,String)

  type coordenadas = (Double,Double)

  //(LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
 // var listaResultante: List[LocalDate, Location]

  var mapita = scala.collection.mutable.Map[STNWBAN, coordenadas]()


  while (lines.hasNext) {

    val linea = lines.next()
    val trozos: List[String] = linea.split(",").toList
    if (trozos.size == 4) mapita((trozos(0),trozos(1))) = (trozos(2).toDouble,trozos(3).toDouble)

  }


  val is2 :InputStream = getClass.getResourceAsStream("/prueba/1977.csv")
  val lines2: Iterator[String] = scala.io.Source.fromInputStream( is2 ).getLines()
  case class Location(lat: Double, lon: Double)

  def farenheitToCelsios(temperature: Double):Double = ((temperature - 32) * 5 / 9)

  var fruits = new ListBuffer[(LocalDate,Location,Double)]()

  while (lines2.hasNext) {

      val linea2 = lines2.next()
      val trozos2: List[String] = linea2.split(",").toList
      if (trozos2.size == 5 &&  mapita.contains((trozos2(0),trozos2(1))))
        {
         val location: coordenadas = mapita((trozos2(0),trozos2(1)))
         val pepito=   (LocalDate.of(1977, trozos2(2).toInt, trozos2(3).toInt), Location(location._1,location._2), farenheitToCelsios(trozos2(4).toDouble) )
          fruits += pepito
        }

    }
val pepito = fruits.toSeq

 val juanito =  pepito.map(x=> (x._2,x._3)).groupBy(x=>x._1).map(x=>(x._1,x._2.map(x => x._2))).map(x=>(x._1,x._2.sum/x._2.size)).toSeq

  print(juanito)





}
