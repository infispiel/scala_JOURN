import java.text.SimpleDateFormat
import java.util.{Calendar, Dictionary}
import CONSTS._

import better.files.File

object Journal extends App {
  lazy val todaysFile: File = GetFile(GetFilename(GetTodaysPrefix(stdPrefix)))
  lazy val dbFile: File = File("tmp.JDB")

  def GetAllJournFiles(directory:File): List[File] = {
    val d = directory
    if (d.exists && d.isDirectory) {
      d.glob("*.JOURN").toList
    } else {
      List[File]()
    }
  }

  // How would I extend this to get other dates?
  def GetTodaysPrefix(format: SimpleDateFormat): String = {
    format.format(Calendar.getInstance().getTime)
  }

  def GetFilename(prefix:String): String = {
    prefix + ".JOURN"
  }

  def GetFile(filename:String): File = {
    File(filename).createIfNotExists()
  }

  def checkIfDateExists(filename:String): Boolean = {
    val f:File = personalDirectory / filename
    f.exists
  }

  def openVim(file:File): Int = {
    val processBuilder = new ProcessBuilder("/usr/bin/vim",file.path.toString)
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
    processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)

    val p = processBuilder.start()
    // wait for termination.
    p.waitFor()
  }

  override
  def main(args: Array[String]): Unit = {
    def printUsage() {
      println("Usage goes here")
    }

    if (args.length == 0) { printUsage(); sys.exit(1) }
    val arglist = args.toList
    type OptionMap = Map[Symbol, Any]

    // Find the mode user wants us to go into
    args.toList match {
      case "today" :: Nil => openVim(todaysFile)
      case "keywords" :: tail => // keywords stuff
      case "open" :: tail => // open stuff
      case _ =>
        println("Unsure what to do with the following arguments: " + _)
        printUsage()
        sys.exit(1)
    }

  }
}
