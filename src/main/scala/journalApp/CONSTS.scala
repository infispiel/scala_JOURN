package journalApp

import java.text.SimpleDateFormat

import better.files.File

import scala.util.matching.Regex

object CONSTS {

  //val personalDirectory: File = File.home / "Documents" / "journal_program" / "python"
  val personalDirectory: File = File.currentWorkingDirectory
  val stdPrefix: SimpleDateFormat = new SimpleDateFormat("MM_dd_yy")
  val prefexReg:Regex = """[0-1][1-9]_[0-2][1-9]_[0-9][0-9]""".r

  // "word to file separator"
  val WtFS = ";"
  // "file to line separator"
  val FtLS = ":"
  // "file to file separator"
  val FtFS = ","

  type OptionMap = Map[Symbol, Any]

  def getNameFromPath(file:File): String ={
    prefexReg.findFirstMatchIn(file.pathAsString) match { case pref => s"$pref"}
  }
}
