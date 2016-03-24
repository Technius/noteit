name := """noteit"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  evolutions,
  "org.postgresql" % "postgresql" % "9.4-1203-jdbc42",
  "io.getquill" %% "quill-jdbc" % "0.5.0",
  "io.getquill" %% "quill-async" % "0.5.0",
  "org.webjars" % "pure" % "0.6.0"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

pipelineStages := Seq(digest, gzip)
