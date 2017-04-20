package observatory

import java.time.LocalDate

import org.apache.spark.sql.{Encoders, Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}

object Main extends App {

  val year:Int = 1977
  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Time Usage")
      .config("spark.master", "local")
      .getOrCreate()

  import spark.implicits._

/** DS start
  val keyValues = List((3,"Me"), (1,"Thi"),(2,"Se"),(3,"ssa"),(1,"sIsA"),(3,"ge:"),
    (3,"-)"),(2,"cre"),(2,"t"))
  val keyValueDS= keyValues.toDS()

  val arraySecreto = keyValueDS.groupByKey(p=>p._1).mapGroups((k,vs)=>(k,vs.foldLeft("")((acc,p)=>acc+p._2))).rdd.sortByKey().map(x=>(x._2+" ")).collect()

  arraySecreto.foreach(print)

  keyValueDS.show(33,true)
*/
  val sc: SparkContext = spark.sparkContext

  val sqlContext = spark.sqlContext

  val customSchemaTemp = StructType(Array(
    StructField("STN", StringType, nullable = true),
    StructField("WBAN", StringType, nullable = true),
    StructField("month", IntegerType, nullable = true),
    StructField("day", IntegerType, nullable = true),
    StructField("avgTemperature", DoubleType, nullable = true)))

  val dfTemp = sqlContext.read.format("com.databricks.spark.csv").schema(customSchemaTemp).load("src/main/resources/1977.csv").na.fill("0",Seq("STN","WBAN"))


  val customSchemaStations = StructType(Array(
    StructField("STN", StringType, nullable = true),
    StructField("WBAN", StringType, nullable = true),
    StructField("latitud", DoubleType, nullable = true),
    StructField("longitud", DoubleType, nullable = true)))

  val dfStations = sqlContext.read.format("com.databricks.spark.csv").schema(customSchemaStations).load("src/main/resources/stations.csv")



  val dfStationsFiltrada =  dfStations.where(!(dfStations.col("latitud").isNull || dfStations.col("longitud").isNull))

  val dfStationsFiltradaArreglada =  dfStations.where(!(dfStations.col("latitud").isNull || dfStations.col("longitud").isNull)).na.fill("0",Seq("STN","WBAN"))


  dfTemp.show(20)
  dfStations.show(20)
  dfStationsFiltrada.show(20)
  dfStationsFiltradaArreglada.show(22)


  implicit val localDateEncoder = Encoders.kryo[(LocalDate,Location,Double)]


  final case class Body(STN: String,
                        WBAN: String,
                        month: Int,
                        day: Int,
                        avgTemperature: Double,
                        latitud: Double,
                        longitud: Double)

  val ds = dfTemp.join(dfStationsFiltradaArreglada,Seq("STN","WBAN"),"inner").as[Body].
    rdd.map(x=>(LocalDate.of(year,x.month,x.day), Location(x.latitud,x.longitud),x.avgTemperature)).collect().toSeq.foreach(println)

 // ds.map(x=>(LocalDate.of(year,x.month,x.day)))


  /*  Location(x.latitud,x.longitud),x.avgTemperature
    ))(localDateEncoder).show(200)*/




  /**.map(r =>

    (LocalDate.of(year,r.getInt(3),r.getInt(2)),
      Location(r.getDouble(5),r.getDouble(6)),r.getDouble(4)
      ),localDateEncoder


  )..limit(300).show(300)
*/



}
