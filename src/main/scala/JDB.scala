import CONSTS._
import better.files.File

object JDB {
  val DBFile:File = CONSTS.personalDirectory / "tmp.JDB"

  def LoadDB(DBFile:File): Map[String,Array[String]] = {
    DBFile.lines
      .filter(line => line.startsWith("#"))
      .map(_.substring(1))
      .map((line:String)=>{
        val a = line.split(':')(0)
        val b = line.split(':')(1).split(',')
        Map(a->b)
      }:Map[String,Array[String]])
      .fold(Map.empty)(_ ++ _)
  }

  lazy val jdb:Map[String, Array[String]] = LoadDB(DBFile)
  lazy val dbIsEmpty:Boolean = jdb.keys.isEmpty

  private def getKeywordFiles(word:String):Array[String] = {
    jdb(word)
  }

  def printKeywordFiles(word:String):Unit = {
    if(dbIsEmpty) println("No keys to search."); sys.exit(0)
    // TODO: Add behavior if no files are found.
    println("Files containing the word " + word + ":")
    println(getKeywordFiles(word).fold("")(_ + ", " + _).substring(2))
  }

  def listKeywords(options:OptionMap): Unit = {
    if(dbIsEmpty) {println("No keys to list."); sys.exit(0)}
    val start_val:Int = (options getOrElse('n_start, 0)).toString.toInt
    if(start_val > jdb.keys.size) {println("ERR: Start requested is higher than the # of entries."); sys.exit(1)}
    if(start_val < 0) {println("ERR: Start value must be >= 0."); sys.exit(1)}

    var end_val:Int = (options getOrElse('n_view, 10)).toString.toInt + 1
    if(end_val > jdb.keys.size) end_val = jdb.keys.size


    println("Viewing keys from " + (start_val + 1) + " to " + end_val + "...")
    println(jdb.keys.slice(start_val, end_val)
      .fold("")(_ + ", " + _)
      .substring(2))

  }

}
