name := "pg-exporter"

ThisBuild / version := "0.2.1"
ThisBuild / scalaVersion := "2.12.18"

Compile / run / fork := true
Compile / run / javaOptions ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)

val sparkVersion = "3.5.0"

libraryDependencies ++= Seq(
  "org.postgresql"    % "postgresql"    % "42.5.4",
  "org.apache.hadoop" % "hadoop-azure"  % "3.3.4",
  "org.apache.hadoop" % "hadoop-common" % "3.3.4"      % Provided,
  "io.delta"         %% "delta-core"    % "2.2.0",
  "org.apache.spark" %% "spark-sql"     % sparkVersion % Provided
)
