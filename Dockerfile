FROM gcr.io/spark-operator/spark:v3.1.1-hadoop3

ADD target/scala-2.12/pg-exporter-assembly-0.2.jar /opt/spark/work-dir/pg-exporter.jar


