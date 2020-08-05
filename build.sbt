name := "pg-exporter"

version := "0.1"

scalaVersion := "2.12.12"

fork in run := true
javaOptions in run ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)

val sparkVersion = "3.0.0"

libraryDependencies ++= Seq(
  "org.postgresql"      % "postgresql"      % "42.2.14",
  "org.apache.hadoop"   % "hadoop-azure"    % "3.3.0",
  "org.apache.hadoop"   % "hadoop-common"   % "3.3.0",
  "io.delta"           %% "delta-core"      % "0.7.0",
  "org.apache.spark"   %% "spark-streaming" % sparkVersion % Provided,
  "org.apache.spark"   %% "spark-sql"       % sparkVersion % Provided
)
