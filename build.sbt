name := "journal_app"

version := "0.1"

scalaVersion := "2.12.8"

val betterFilesVersion = "3.6.0"

libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % betterFilesVersion
)

mainClass in assembly :=Some("journalApp.Journal")
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
