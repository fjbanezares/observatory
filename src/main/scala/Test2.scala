import java.io.InputStream
import java.nio.file.Paths
import java.time.LocalDate

import observatory.Location

import scala.collection.mutable.ListBuffer

object Test2 extends App {

  def distance(location: Location, location2: Location): Double = 2

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    *
    *
    * def gcDistance(r: Double)(loc1: Location, loc2: Location)

Same goes for pixel <-> Location conversion.

def locationToPixel(imgWidth: imgHeight)(loc: Location): (Int, Int)

def pixelToLocation(imgWidth: Int, imgHeight: Int)(int x, int y): Location
    *
    *         sum(wi*zi)/sum(wi)
    *
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val factoresarriba = for {
      tempe <- temperatures

    } yield{

      tempe._2/Math.pow(distance(location, tempe._1),2)}

    val factoresabajo = for {
      tempe <- temperatures

    } yield(1/Math.pow(distance(location, tempe._1),2))

    return factoresarriba.reduce(_+_)/factoresabajo.reduce(_+_)
  }

val prediccion = predictTemperature(List((Location(1,2),12),(Location(1,2),2)), Location(1,1))
  println(prediccion)




}
