package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
      *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = d00*(1-x)*(1-y) + d10*x*(1-y) + d01*(1-x)*y + d11*x*y


  /**
    * idea de foros
    * point(ceil(lat), floor(lon)), point(ceil(lat), ceil(lat)), point(floor(lat), floor(lon)), point(floor(lat), ceil(lon))
    *
    *
    *
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize  LONGITUD -180 +180
    * @param y Y value of the tile to visualize  LATITUD -90 + 90
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {
  

    val arrayConstructor:Array[Pixel] = (for {
      longitud <- 0 until 256
      latitud <- 0 until 256
    } yield {
      val punto:Location = Interaction.tileLocation(zoom + 8, x * 256 + latitud, y * 256 + longitud)
      val d00: Double = grid(Math.floor(punto.lon).toInt, Math.ceil(punto.lat).toInt)
      val d01: Double = grid(Math.floor(punto.lon).toInt, Math.floor(punto.lat).toInt)
      val d10: Double = grid(Math.ceil(punto.lon).toInt, Math.ceil(punto.lat).toInt)
      val d11: Double = grid(Math.ceil(punto.lon).toInt, Math.floor(punto.lat).toInt)
      val equis: Double = punto.lon-Math.floor(punto.lon)
      val yausar: Double = punto.lat.ceil - punto.lat
      val color: Color = Visualization.interpolateColor(colors, bilinearInterpolation(equis,yausar,d00,d01,d10,d11))
      Pixel(color.red, color.green, color.blue, 127)
    }).toArray

    Image(256,256,arrayConstructor)


  }

}
