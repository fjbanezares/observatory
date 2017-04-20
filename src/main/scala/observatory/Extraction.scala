package observatory

import java.time.LocalDate

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Encoders, SQLContext, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}


/**
  * 1st milestone: data extraction
  */
object  Extraction {

  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Time Usage")
      .config("spark.master", "local")
      .getOrCreate()


  import spark.implicits._

  val sc: SparkContext = spark.sparkContext
  final case class Body(STN: String,
                        WBAN: String,
                        month: Int,
                        day: Int,
                        avgTemperature: Double,
                        latitud: Double,
                        longitud: Double)
  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    *
    *         TermperaturesFile : (010010,,01,01,7.5) --> 1 de enero, 7.5 grados de media
    *         De un fichero y las estaciones nos da
    *
    *         stationsFile: (010860,,+70.600,+029.693)
    *
    *         case class Location(lat: Double, lon: Double)
    *
    *
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    import spark.implicits._

    val sqlContext = spark.sqlContext

    val customSchemaTemp = StructType(Array(
      StructField("STN", StringType, true),
      StructField("WBAN", StringType, true),
      StructField("day", IntegerType, true),
      StructField("month", IntegerType, true),
      StructField("avgTemperature", DoubleType, true)))
    val dfTemperature = sqlContext.read.format("com.databricks.spark.csv").schema(customSchemaTemp).load(temperaturesFile).na.fill("0",Seq("STN","WBAN"))


    val customSchemaStations = StructType(Array(
      StructField("STN", StringType, true),
      StructField("WBAN", StringType, true),
      StructField("lat", DoubleType, true),
      StructField("long", DoubleType, true)))
    val dfStations = sqlContext.read.format("com.databricks.spark.csv").schema(customSchemaStations).load(stationsFile)

    val dfStationsFiltrada =  dfStations.where(!(dfStations.col("lat").isNull || dfStations.col("long").isNull))
    val dfStationsFiltradaArreglada =  dfStations.where(!(dfStations.col("lat").isNull || dfStations.col("long").isNull)).na.fill("0",Seq("STN","WBAN"))

    implicit val localDateEncoder = Encoders.kryo[(LocalDate,Location,Double)]





    dfTemperature.join(dfStationsFiltradaArreglada,Seq("STN","WBAN"),"inner").as[Body].
      rdd.map(x=>(LocalDate.of(year,x.month,x.day), Location(x.latitud,x.longitud),x.avgTemperature)).collect().toSeq

    //
  }


  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    val rddIterable = sc.parallelize(records)

}
