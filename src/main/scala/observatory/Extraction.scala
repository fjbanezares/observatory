package observatory

import java.time.LocalDate

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StructField, StructType, IntegerType, DoubleType, StringType}
import org.apache.spark.sql.SparkSession
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
  val sc: SparkContext = spark.sparkContext

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

    val sqlContext = spark.sqlContext

    val customSchemaTemp = StructType(Array(
      StructField("STN", StringType, true),
      StructField("WBAN", StringType, true),
      StructField("day", IntegerType, true),
      StructField("month", IntegerType, true),
      StructField("avgTemperature", DoubleType, true)))

    val dfTemp = sqlContext.read.format("com.databricks.spark.csv").schema(customSchemaTemp).load(temperaturesFile).na.fill("0",Seq("STN","WBAN"))


    val customSchemaStations = StructType(Array(
      StructField("STN", StringType, true),
      StructField("WBAN", StringType, true),
      StructField("lat", DoubleType, true),
      StructField("long", DoubleType, true)))

    val dfStations = sqlContext.read.format("com.databricks.spark.csv").schema(customSchemaStations).load(stationsFile)



    val dfStationsFiltrada =  dfStations.where(!(dfStations.col("lat").isNull || dfStations.col("long").isNull))

    val dfStationsFiltradaArreglada =  dfStations.where(!(dfStations.col("lat").isNull || dfStations.col("long").isNull)).na.fill("0",Seq("STN","WBAN"))


    dfTemp.show(20)
    dfStations.show(20)
    dfStationsFiltrada.show(20)
    dfStationsFiltradaArreglada.show(22)


    dfTemp.join(dfStationsFiltradaArreglada,Seq("STN","WBAN"),"inner")




    //

???
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

}
