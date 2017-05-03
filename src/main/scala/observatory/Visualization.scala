package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import Math.sin
import Math.acos
import Math.cos
import Math.PI

/**
  * 2nd milestone: basic visualization
  */
object Visualization {


  val white60: Color = Color(255, 255, 255)
  val red32: Color = Color(255, 0, 0)
  val yellow12: Color = Color(255, 255, 0)
  val cyan0: Color = Color(0, 255, 255)
  val blue15M: Color = Color(0, 0, 255)
  val magenta27M: Color = Color(255, 0, 255)
  val purple50M: Color = Color(33, 1, 107)
  val black60M: Color = Color(0, 0, 0)

 val ejemploColors: Iterable[(Double, Color)] = List((-60,black60M),(-50,purple50M),(-27,magenta27M),(-15,blue15M),
   (0,cyan0),(12,yellow12),(32,red32),(60,white60))


  def distance(location: Location, location2: Location): Double = {
    val lat1:Double=location.lat*PI/180
    val lat2:Double=location2.lat*PI/180
    val lon1:Double=location.lon*PI/180
    val lon2:Double=location2.lon*PI/180
    acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon2-lon1))*18000/PI
  }
//Put it in degrees so distances are not too small in order to avoid great numbers in the prediction algorithm
  //Plus a factor of 100




  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    *
    *
    *         sum(wi*zi)/sum(wi)
    *
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {

     val tempsOrderedbyDistance = temperatures.toList.map(x=>(Math.pow(distance(location,x._1),3),x._2)).sortBy(x=>x._1)
//Si la distancia incrementada y elevada al cubo es menor que 1 (lo que daría un numero grande de divisor)
    //Para una distancia tan pequeña devolvemos la temperatura de la estación más cercana
    if (tempsOrderedbyDistance.head._1<1) tempsOrderedbyDistance.head._2
    else {
       val factoresarriba = tempsOrderedbyDistance.foldLeft(0.0)((acc,elemento)=>(acc+elemento._2/elemento._1))
       val factoresabajo = tempsOrderedbyDistance.foldLeft(0.0)((acc,elemento)=>(acc+1/elemento._1))

    factoresarriba/factoresabajo

    }


  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val pointsOrdered: List[(Double, Color)] = points.toList.sortBy(_._1)

    def loop(pointsLoop: List[(Double, Color)], lastColor: Color, lastValue: Double): Color = {

      if (pointsLoop == Nil) lastColor

      else if (value < pointsLoop.head._1) {

        val rojo: Int = (lastColor.red + (value - lastValue) * (pointsLoop.head._2.red - lastColor.red) / (pointsLoop.head._1 - lastValue)).round.toInt
        val verde: Int = (lastColor.green + (value - lastValue) * (pointsLoop.head._2.green - lastColor.green) / (pointsLoop.head._1 - lastValue)).round.toInt
        val azul: Int = (lastColor.blue + (value - lastValue) * (pointsLoop.head._2.blue - lastColor.blue) / (pointsLoop.head._1 - lastValue)).round.toInt
        Color(rojo, verde, azul)

      }

      else loop(pointsLoop.tail, pointsLoop.head._2, pointsLoop.head._1)

    }
    if (value < pointsOrdered.head._1) pointsOrdered.head._2
    else loop(pointsOrdered.tail, pointsOrdered.head._2, pointsOrdered.head._1)
  }
  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    *
    *         Pixel(our Color type)
    *
    *
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    //
    // construct array

    val arrConst: Array[Pixel] = new Array(360 * 180)

    for {

      coordenadaY <- 0 until 180  //de 0 a 179
      coordenadaX <- 0 until 360 // de 0 a 359

    } {
      val posicionArray: Int = coordenadaY * 360 + coordenadaX
      val coordenadas: Location = Location(90 - coordenadaY, coordenadaX - 180)
      val prediccion = predictTemperature(temperatures, coordenadas)
      val color: Color = interpolateColor(colors, prediccion)
      val pix = Pixel(color.red, color.green, color.blue, 255)
      arrConst(posicionArray) = pix

    }
    println("a dibujar, " + arrConst(180*360-1))
    println("go")

    Image(360, 180, arrConst)
  }


}
