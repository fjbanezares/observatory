package observatory

import java.time.LocalDate

import org.apache.spark.rdd.RDD
import java.io.File
import java.nio.file.Paths

import org.apache.spark.sql._
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

  val sc = spark.sparkContext

  import spark.implicits._


  def fsPath(resource: String): String = Paths.get(getClass.getResource(resource).toURI).toString
  def rowTemp(line: List[String]): Row = Row(line(0), line(1), line(2).toInt, line(3).toInt, line(4).toDouble)
  def rowStat(line: List[String]): Row = Row(line(0), line(1), line(2).toDouble, line(3).toDouble)

  def readTemp(resource: String): DataFrame = {
    val rdd = spark.sparkContext.textFile((resource))

    // Compute the schema based on the first line of the CSV file
    val customSchemaTemp = StructType(Array(
      StructField("STN", StringType, true),
      StructField("WBAN", StringType, true),
      StructField("month", IntegerType, true),
      StructField("day", IntegerType, true),
      StructField("avgTemperature", DoubleType, true)))

    val data = rdd.map(_.split(",").to[List]).filter(x=>x.size==5).map(rowTemp)

    val dataFrame =
      spark.createDataFrame(data, customSchemaTemp)

    dataFrame
  }

  def readStat(resource: String): DataFrame = {
    val rdd = spark.sparkContext.textFile((resource))

    // Compute the schema based on the first line of the CSV file
    val customSchemaStations = StructType(Array(
      StructField("STN", StringType, true),
      StructField("WBAN", StringType, true),
      StructField("latitud", DoubleType, true),
      StructField("longitud", DoubleType, true)))

    val data = rdd.map(_.split(",").to[List]).filter(x=>x.size==4).map(rowStat)

    val dataFrame =
      spark.createDataFrame(data, customSchemaStations)

    dataFrame
  }


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

   val dfTemperature = readTemp(temperaturesFile).na.fill("0",Seq("STN","WBAN"))
   val dfStations = readStat(stationsFile)

   val dfStationsFiltrada =  dfStations.where(!(dfStations.col("latitud").isNull || dfStations.col("longitud").isNull))
   val dfStationsFiltradaArreglada =  dfStations.where(!(dfStations.col("latitud").isNull || dfStations.col("longitud").isNull)).na.fill("0",Seq("STN","WBAN"))

//    implicit val localDateEncoder = Encoders.kryo[(LocalDate,Location,Double)]





    dfTemperature.join(dfStationsFiltradaArreglada,Seq("STN","WBAN"),"inner").as[Body].
      rdd.map(x=>(LocalDate.of(year,x.month,x.day), Location(x.latitud,x.longitud),x.avgTemperature)).collect().toSeq

    //
  }


  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] =
    records.map(x=> (x._2,x._3)).groupBy(x=>x._1).map(x=>(x._1,x._2.map(x => x._2))).map(x=>(x._1,x._2.sum/x._2.size))

}
