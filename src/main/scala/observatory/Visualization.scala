package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  def distance(location: Location, location2: Location): Double = 2

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    *
    *
    *         sum(wi*zi)/sum(wi)
    *
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val factoresarriba = for {
      tempe <- temperatures

    } yield(tempe._2/Math.pow(distance(location, tempe._1),2))

    val factoresabajo = for {
      tempe <- temperatures

    } yield(1/Math.pow(distance(location, tempe._1),2))

    return factoresarriba.reduce(_+_)/factoresabajo.reduce(_+_)
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

}

