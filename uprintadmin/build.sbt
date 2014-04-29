
lazy val grunt = taskKey[Unit]("Run Grunt") 

grunt := {
  val log = streams.value.log
  log.warn("A warning.")
  Grunt.run(baseDirectory.value)
}

name := "uprintadmin"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  "org.reactivemongo" %% "reactivemongo" % "0.10.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  cache
)     

