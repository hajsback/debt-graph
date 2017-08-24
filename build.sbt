name := "debt-graph"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.19",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.19",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9",
  "com.typesafe.play" %% "play-json" % "2.6.3"
)

libraryDependencies ++= Seq(
  "com.codemettle.reactivemq" % "reactivemq_2.11" % "1.0.0",
  "org.apache.activemq" % "activemq-client" % "5.13.3",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)
