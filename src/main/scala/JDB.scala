import CONSTS._
import better.files.File

object JDB {
  val DBFile:File = CONSTS.personalDirectory / "keywords.jdb"

  def LoadDB(DBFile:File): Map[String,Array[String]] = {
    DBFile.lines
      .filter(line => line.startsWith("#"))
      .map((line:String)=>{
        val a = line.split(':')(0)
        val b = line.split(':')(1).split(',')
        Map(a->b)
      }:Map[String,Array[String]])
      .fold(Map.empty)(_ ++ _)
  }

  lazy val jdb:Map[String, Array[String]] = LoadDB(DBFile)

  def getKeywordFiles(word:String):Array[String] = {
    jdb(word)
  }


}
