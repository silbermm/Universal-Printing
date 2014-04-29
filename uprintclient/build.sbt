
name := "uprintclient"

val spring_version    = "4.0.0.RELEASE"

retrieveManaged := true

libraryDependencies ++= Seq(
  "commons-cli" % "commons-cli" % "1.2",
  "com.typesafe" % "config" % "1.0.2",
  "org.springframework" % "spring-core" % spring_version,
  "org.springframework" % "spring-context" % spring_version,
  "cglib" % "cglib" % "2.2.2",
  "com.jgoodies" % "looks" % "2.2.2",
  "com.jgoodies" % "forms" % "1.2.1",
  "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3",
  "com.typesafe.akka" % "akka-remote_2.10" % "2.2.3"
)