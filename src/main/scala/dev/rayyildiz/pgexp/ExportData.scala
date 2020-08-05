package dev.rayyildiz.pgexp

import org.apache.spark.sql.SparkSession

object ExportData extends App with SparkSupport with DatabaseSupport {
  private val session: SparkSession = spark(azAccountName, azAccountKey)

  val dataDF = session.read
    .format("jdbc")
    .option("url", s"jdbc:postgresql://$dbUrl:$dbPort/$dbName?user=$dbUsername&password=$dbPassword")
    .option("driver", "org.postgresql.Driver")
    .option("query", "select * from messages")
    .option("fetchsize", 500)
    .option("numPartitions", 10)
    .load()

  dataDF.printSchema()

  dataDF.write
    .partitionBy("language")
    .format("delta")
    .option("checkpointLocation", "/tmp/checkpoint")
    .save(exportPath)

  waitForEnter
  close(session)
}
