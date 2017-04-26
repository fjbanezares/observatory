package observatory

import com.sksamuel.scrimage.{Image, Pixel}

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


  def distance(location: Location, location2: Location): Double = 2

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
    val factoresarriba = for {
      tempe <- temperatures

    } yield (tempe._2 / Math.pow(distance(location, tempe._1), 2))

    val factoresabajo = for {
      tempe <- temperatures

    } yield (1 / Math.pow(distance(location, tempe._1), 2))

    return factoresarriba.reduce(_ + _) / factoresabajo.reduce(_ + _)
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val pointsOrdered: List[(Double, Color)] = points.toList.sortBy(_._1)
    def loop(points: List[(Double, Color)], lastColor: Color, lastValue: Double): Color = {

      if (points == Nil) lastColor

      else if (value < points.head._1) {

        val rojo: Int = (lastColor.red + (value - lastValue) * (points.head._2.red - lastColor.red) / (points.head._1 - lastValue)).toInt
        val verde: Int = (lastColor.green + (value - lastValue) * (points.head._2.green - lastColor.green) / (points.head._1 - lastValue)).toInt
        val azul: Int = (lastColor.blue + (value - lastValue) * (points.head._2.blue - lastColor.blue) / (points.head._1 - lastValue)).toInt
        Color(rojo, verde, azul)

      }

      else loop(points.tail, points.head._2, points.head._1)

    }
    loop(pointsOrdered, pointsOrdered.head._2, pointsOrdered.head._1)
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

      coordenadaY <- 0 until 180
      coordenadaX <- 0 until 360

    } {
      val posicionArray: Int = coordenadaY * 180 + coordenadaX
      val coordenadas: Location = Location(90 - coordenadaY, coordenadaX - 180)
      val prediccion = predictTemperature(temperatures, coordenadas)
      val color: Color = interpolateColor(colors, prediccion)
      val pix = Pixel(color.red, color.green, color.blue, 255)
      arrConst(posicionArray) = pix

    }
    Image(360, 180, arrConst)
  }


}
