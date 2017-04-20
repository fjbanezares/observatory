
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{StructField, StructType, IntegerType, DoubleType, StringType}
import org.apache.spark.sql._



import scala.io
println("Month, Income, Expenses, Profit")
val bufferedSource = io.Source.fromFile("/home/fjbanez/Desktop/curso Spark Oderski/final/observatory/src/main/resources/1977.csv")
for (line <- bufferedSource.getLines) {
  val cols = line.split(",").map(_.trim)
  // do whatever you want with the columns here
  println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
}
bufferedSource.close

val conf: SparkConf = new SparkConf().setMaster("local").setAppName("javito")
val sc: SparkContext = new SparkContext(conf)

// declaring data types
case class Company(cName: String, cId: String, details: String)
case class Employee(name: String, id: String, email: String, company: Company)

// setting up example data
val e1 = Employee("n1", null, "n1@c1.com", Company("c1", "1", "d1"))
val e2 = Employee("n2", "2", "n2@c1.com", Company("c1", "1", "d1"))
val e3 = Employee("n3", "3", "n3@c1.com", Company("c1", "1", "d1"))
val e4 = Employee("n4", "4", "n4@c2.com", Company("c2", "2", "d2"))
val e5 = Employee("n5", null, "n5@c2.com", Company("c2", "2", "d2"))
val e6 = Employee("n6", "6", "n6@c2.com", Company("c2", "2", "d2"))
val e7 = Employee("n7", "7", "n7@c3.com", Company("c3", "3", "d3"))
val e8 = Employee("n8", "8", "n8@c3.com", Company("c3", "3", "d3"))
val employees = Seq(e1, e2, e3, e4, e5, e6, e7, e8)
val df = sc.parallelize(employees)