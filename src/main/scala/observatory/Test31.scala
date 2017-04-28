package observatory

import scala.math._

/**
  * Created by fjbanez on 28/04/17.
  */
object Test31 extends App{
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {

    val numberTiles = Math.pow(2,zoom)
    val latitude: Double = toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1<<zoom)))))
    val longitude: Double =  x.toDouble / (1<<zoom) * 360.0 - 180.0
    Location(latitude,longitude)
  }

  //tileLocation(zoom, x, y)
  println(tileLocation(2,0,3))
}
