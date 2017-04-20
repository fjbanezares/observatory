
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{StructField, StructType, IntegerType, DoubleType, StringType}
import org.apache.spark.sql._

val conf: SparkConf = new SparkConf().setMaster("local").setAppName("javito")
val sc: SparkContext = new SparkContext(conf)

