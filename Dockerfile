FROM gcr.io/spark-operator/spark:v3.0.0-hadoop3

ADD target/scala-2.12/pg-exporter-assembly-0.1.jar /opt/spark/work-dir/pg-exporter.jar


