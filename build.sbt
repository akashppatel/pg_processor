
name := "pg_processor"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "2.4.11",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
  "commons-codec" % "commons-codec" % "1.10",
  "net.liftweb" %% "lift-json" % "3.0-RC3",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-classic" % "1.1.7" % "runtime")

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.11",
    "com.typesafe.akka" %% "akka-http-testkit" % "2.4.11",
    "org.scalatest" % "scalatest_2.11" % "3.0.0",
    "com.typesafe.akka" %% "akka-testkit" % "2.4.11" % "test" )

scalacOptions in Test ++= Seq("-Yrangepos", "-Ylog-classpath")
