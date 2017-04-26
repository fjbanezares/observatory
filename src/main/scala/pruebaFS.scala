import java.nio.file.Paths

/**
  * Created by fjbanez on 26/04/17.
  */
object pruebaFS extends App{




    def fsPath(resource: String): String =
      Paths.get(getClass.getResource(resource).toURI).toString



  println(fsPath("/prueba/stations.csv"))
}
