name := "journal_app"

version := "0.1"

scalaVersion := "2.12.8"

val betterFilesVersion = "3.6.0"

libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % betterFilesVersion
)

mainClass in assembly :=Some("journalApp.Journal")
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)