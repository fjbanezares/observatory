package observatory

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with BeforeAndAfterAll{


  def initializeExtraction(): Boolean =
    try {
      Extraction
      true
    } catch {
      case ex: Throwable =>
        println(ex.getMessage)
        ex.printStackTrace()
        false
    }

  override def afterAll(): Unit = {
    assert(initializeExtraction(), " -- did you fill in all the values in Extraction (conf, sc, wikiRdd)?")
    import Extraction._
  }

  //def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)]
  test("extracción Temperaturas") {
    assert(initializeExtraction(), " -- did you fill in all the values Extraction?")
    import Extraction._
    val res = ((locateTemperatures(1977, "/prueba/stations.csv", "/prueba/1977.csv").last) == ((LocalDate.of(1977, 12, 31), Location(32.867, -117.133), 57.1)))
    assert(res, "El último registro debe tener el valor: (LocalDate.of(1977, 12, 31), Location(32.867, -117.133), 57.1)")
  }

  
}