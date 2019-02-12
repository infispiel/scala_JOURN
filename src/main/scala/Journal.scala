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
    format.format(Calendar.getInstance().getTime())
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

    println(GetAllJournFiles(personalDirectory))
//  openVim(todaysFile)
}
