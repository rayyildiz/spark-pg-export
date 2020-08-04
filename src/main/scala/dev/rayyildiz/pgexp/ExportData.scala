package dev.rayyildiz.pgexp

object ExportData extends App with SparkSupport with DatabaseSupport {

  val dataDF = spark.read
    .format("jdbc")
    .option("url", s"jdbc:postgresql://$dbUrl:$dbPort/$dbName?user=$dbUsername&password=$dbPassword")
    .option("driver", "org.postgresql.Driver")
    .option("query", "select * from messages")
    .option("fetchsize", "50")
    .load()

  dataDF.printSchema()

  dataDF.write
    .partitionBy("language")
    .format("parquet")
    .save(exportPath)

  waitForEnter
  close()
}
