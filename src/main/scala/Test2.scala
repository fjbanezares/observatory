import java.io.InputStream
import java.nio.file.Paths
import java.time.LocalDate

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.{Color, Location}

import scala.collection.mutable.ListBuffer

object Test2 extends App {

  val white60: Color = Color(255,255,255)
  val red32: Color = Color(255,0,0)
  val yellow12: Color = Color(255,255,0)
  val cyan0: Color = Color(0,255,255)
  val blue15M: Color = Color(0,0,255)
  val magenta27M: Color = Color(255,0,255)
  val purple50M: Color = Color(33,1,107)
  val black60M: Color = Color(0,0,0 )

  val points:List[(Double,Color)] = List((60d,white60),  (12d,yellow12),(32d,red32),
      (0d,cyan0),(-15d,blue15M),(-27d,magenta27M), (-50d,purple50M), (-60d,black60M))



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
    *(Location(67.55,-63.783),-11.08749168330008), (Location(45.933,126.567),4.501522070015218),
    * (Location(28.967,118.867),17.365625956535045),
    * (Location(40.45,75.383)
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


  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {

      val pointsOrdered:List[(Double,Color)] = points.toList.sortBy(_._1)


    def loop(points: List[(Double, Color)], lastColor:Color, lastValue: Double):Color = {

      if (points == Nil )  lastColor
      else if (value < points.head._1) {

        val rojo:Int = (lastColor.red + (value-lastValue) * (points.head._2.red-lastColor.red)/(points.head._1-lastValue)).toInt
        val verde:Int = (lastColor.green + (value-lastValue) * (points.head._2.green-lastColor.green)/(points.head._1-lastValue)).toInt
        val azul:Int = (lastColor.blue + (value - lastValue) * (points.head._2.blue-lastColor.blue)/(points.head._1-lastValue)).toInt
        Color(rojo,verde,azul)

      }

      else loop(points.tail,points.head._2,points.head._1)

          }

  /**  case x if ( x >= 60) => white60
    case x if (0 < 60 && x >= 32) => {
      val rojo:Int = (red32.red + (x-32) * (white60.red-red32.red)/(28)).toInt
      val verde:Int = (red32.green + (x-32) * (white60.green-red32.green)/(28)).toInt
      val azul:Int = (red32.blue + (x-32) * (white60.blue-red32.blue)/(28)).toInt
      Color(rojo,verde,azul)

    }*/
loop(pointsOrdered,pointsOrdered.head._2,pointsOrdered.head._1)
  }

  println(interpolateColor(points,-17))



  def visualize(): Image = {

    //construcción del array temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]

    //creación de la imagen


    //def apply(w: Int, h: Int, pixels: Array[Pixel]): Image

    val pixel1: Pixel = Pixel(red32.red,red32.green,red32.blue,255)
    val pixel2: Pixel = Pixel(black60M.red,black60M.green,black60M.blue,255)
    val pixel3: Pixel = Pixel(yellow12.red,yellow12.green,yellow12.blue,255)
    val pixel4: Pixel = Pixel(magenta27M.red,magenta27M.green,magenta27M.blue,255)

    Image(2,2,List(pixel1,pixel2,pixel3,pixel4).toArray)
  }

visualize().output(new java.io.File("target/some-image.png"))


}
