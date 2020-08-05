package dev.rayyildiz.pgexp

import java.time.Instant

import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.io.StdIn

trait SparkSupport {
  lazy val log: Logger = LogManager.getLogger(getClass)

  lazy val conf: SparkConf = new SparkConf()
    .setAppName("pgexp")
    .setIfMissing("spark.master", "local[*]")

  def spark(container: String = "", accountKey: String = ""): SparkSession = {

    val builder = SparkSession.builder
      .config(conf)
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")

    if (!container.isEmpty && !accountKey.isEmpty) {
      builder.config(s"fs.azure.account.key.$container.blob.core.windows.net", accountKey)
    }

    val session = builder.getOrCreate()

    session.sparkContext.hadoopConfiguration.set("fs.wasbs.impl", "org.apache.hadoop.fs.azure.NativeAzureFileSystem")
    session
  }

  def waitForEnter: String = {
    Console.println("To finish press [ENTER] key")
    StdIn.readLine()
  }

  def close(session: SparkSession): Unit = {
    log.info("Stopping spark...")
    session.close()
    log.info("Spark server stopped")
  }
}

trait DatabaseSupport {
  def getEnv(key: String, defaultVal: String): String = {
    val value = System.getenv(key)
    if (null == value || value.isEmpty) {
      defaultVal
    } else value
  }

  lazy val dbUrl: String         = getEnv("PG_DATABASE", "localhost")
  lazy val dbPort: Int           = getEnv("PG_PORT", "5432").toInt
  lazy val dbUsername: String    = getEnv("PG_USERNAME", "postgres")
  lazy val dbPassword: String    = getEnv("PG_PASSWORD", "123456")
  lazy val dbName: String        = getEnv("PG_DBNAME", "postgres")
  lazy val azAccountName: String = getEnv("AZ_ACCOUNT_NAME", "")
  lazy val azAccountKey: String  = getEnv("AZ_ACCOUNT_KEY", "")

  private val moment: Long = Instant.now().toEpochMilli

  lazy val exportPath: String = getEnv("EXPORT_PATH", s"/tmp/data/${moment}")
}
