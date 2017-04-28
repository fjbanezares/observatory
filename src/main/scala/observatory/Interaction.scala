package observatory
import scala.math._

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {


  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {

    val numberTiles = Math.pow(2,zoom)
    val latitude: Double = toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1<<zoom)))))
    val longitude: Double =  x.toDouble / (1<<zoom) * 360.0 - 180.0
    Location(latitude,longitude)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {

    /** val topLeft = tileLocation(zoom, x, y)

  val bottomRight = tileLocation(zoom, x + 1, y + 1) ¿¿-1-1??

  val deltaLon = (bottomRight.lon - topLeft.lon) / 256
  val deltaLat = (bottomRight.lat - topLeft.lat) / 256

  Image(
    256,
    256,
    (for (x <- 0 until 256; y <- 0 until 256)
      yield {
        val color = Visualization.interpolateColor(
          colors,
          Visualization.predictTemperature(
            temperatures,
            Location(topLeft.lat + x * deltaLat, topLeft.lon + y * deltaLon)))
        Pixel(color.red, color.green, color.blue, 127)
      }).toArray)*/

    ???
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {
    ???
  }

}
