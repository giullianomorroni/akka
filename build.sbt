name := "hugme-akka-java"

val akkaVersion = "2.3.10"

val scalaVersion = "2.10.4"

libraryDependencies ++= Seq(
  	"com.typesafe.akka" %% "akka-cluster" % akkaVersion,
	"com.typesafe.akka" %% "akka-actor" % akkaVersion,
	"org.slf4j" % "slf4j-simple" % "1.7.12"
)

fork in run := true