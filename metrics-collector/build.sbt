ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
.settings(
    name := "metrics-collector",    
)
.settings(    
    assemblyJarName in assembly := "app.jar",    
    assemblyOutputPath  in assembly := file(s"/var/tmp/export-jar/${(assembly/assemblyJarName).value}"),
)
  
enablePlugins(AssemblyPlugin)
enablePlugins(JavaAppPackaging)

val akkaVersion = "2.6.5"
val akkaHttpVersion = "10.1.12"
val scalatestVersion = "3.2.15"

//fork in run := true

libraryDependencies ++= Seq(
  // akka streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion  % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
)

/*
assemblyMergeStrategy in assembly := {
 case PathList("META-INF", _*) => MergeStrategy.discard
 case x => MergeStrategy.defaultMergeStrategy(x)
}
*/

