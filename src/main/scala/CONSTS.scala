import java.text.SimpleDateFormat

import better.files.File

object CONSTS {

  //val personalDirectory: File = File.home / "Documents" / "journal_program" / "python"
  val personalDirectory: File = File.currentWorkingDirectory
  val stdPrefix: SimpleDateFormat = new SimpleDateFormat("MM_dd_yy")
  type OptionMap = Map[Symbol, Any]
}
