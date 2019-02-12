import java.text.SimpleDateFormat
import java.util.{Calendar, Dictionary}
import CONSTS._

import better.files.File

object Journal extends App {
  lazy val todaysFile: File = GetFile(GetTodaysPrefix(stdPrefix))
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

  def GetFile(filename:String): File = {
    File(filename + ".JOURN").createIfNotExists()
  }

  def checkIfDateExists(filename:String): Boolean = {
    val f:File = personalDirectory / filename
    f.exists
  }

  // TODO : Add keyword detection in new file
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

    /**
      * Iterates through a list of arguments assuming user is in the 'list' mode.
      * Exits with code 1 if an unexpected argument is passed.
      * @param map Current list of parsed parameters & their values
      * @param list Remaining strings to iterate through
      * @return Map[Symbol, Any] of parsed parameters & their values
      */
    def nextListOption(map: OptionMap, list: List[String]) : OptionMap = {
      list match {
        case Nil => map
        case "-s" :: value :: tail =>
          nextListOption(map ++ Map('n_start -> value.toInt), tail)
        case "-n" :: value :: tail =>
          nextListOption(map ++ Map('n_view -> value.toInt), tail)
        case option :: tail => println("Unknown keyword list option " + option)
          printUsage()
          sys.exit(1)
      }
    }

    // Find the mode user wants us to go into
    args.toList match {
      case "today" :: Nil => openVim(todaysFile)
      case "keywords" :: tail =>
        tail match {
          case "search" :: word :: Nil => JDB.searchKeyword(word)
          case "list" :: list_tail =>
            JDB.listKeywords(nextListOption(Map(),list_tail))

        }

      // actually do we really need this? if we want to open a file we just go and open it.
      // this might have been useful using a CURSES lib but not as a command line application.
      // Will implement it for now; maybe we will put .JOURN files into subfolders, in which case this might be
      //  marginally more useful.
      case "open" :: tail => // open stuff
      case _ =>
        println("Unsure what to do with the following arguments: " + _)
        printUsage()
        sys.exit(1)
    }

  }
}
