package observatory

/**
  * Created by fjbanez on 27/04/17.
  */
object Test22 extends App {

 // Color(0,0,0). Expected: Color(255,0,0) (scale = List((-1.0,Color(255,0,0)), (0.0,Color(0,0,255))), value = -11.0)


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

  val points: Iterable[(Double, Color)]= List((-1.0,Color(255,0,0)), (0.0,Color(0,0,255)))
  val value: Double =  -0.75
  println(interpolateColor(points,value))


  def penultimate[A](list: List[A]): A =
    list.foldLeft( (list.head, list.tail.head) )((r, c) => (r._2, c) )._1

  val prueba = List(1,2,3,4,5)
  println(penultimate(prueba))

}
