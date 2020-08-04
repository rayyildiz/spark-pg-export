package dev.rayyildiz.pgexp

import java.time.LocalDate

import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import java.time.format.DateTimeFormatter

import scala.io.StdIn

trait SparkSupport {
  lazy val log: Logger = LogManager.getLogger(getClass)

  lazy val conf: SparkConf = new SparkConf()
    .setAppName("pgexp")
    .setIfMissing("spark.master", "local[*]")

  lazy val spark: SparkSession = SparkSession.builder
    .config(conf)
    .getOrCreate()

  def waitForEnter: String = {
    Console.println("To finish press [ENTER] key")
    StdIn.readLine()
  }

  def close(): Unit = {
    log.info("Stopping spark...")
    spark.close()
    log.info("Spark server stopped")
  }
}


trait DatabaseSupport {
  def getEnv(key:String, defaultVal:String): String = {
    val value = System.getenv(key)
    if (null == value || value.isEmpty) {
      defaultVal
    } else value
  }

  lazy val dbUrl:String  = getEnv("PG_DATABASE","localhost")
  lazy val dbPort:Int = getEnv("PG_PORT","5432").toInt
  lazy val dbUsername:String = getEnv("PG_USERNAME","postgres")
  lazy val dbPassword:String= getEnv("PG_PASSWORD","123456")
  lazy val dbName :String = getEnv("PG_DBNAME","postgres")


  private val formatter: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE
  private val moment :String = formatter.format(LocalDate.now())

  lazy val exportPath:String = getEnv("EXPORT_PATH",s"./export/${moment}")
}
