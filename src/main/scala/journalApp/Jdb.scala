package journalApp

import better.files.File
import journalApp.CONSTS._

import scala.util.matching.Regex
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

class Jdb(DBFile:File) {
  val keywordReg: Regex = """^([0-9]+)#K ([\s]+)$""".r // expected format of a keyword

  lazy val jdb: Database= LoadDB(DBFile)

  def LoadDB(DBFile: File): Database = {
    //decode[Database](DBFile.contentAsString)
    DBFile.lines
      .filter(line => line.startsWith("#"))
      .map(DatabaseEntry.apply)
      .map(dbEntry => (dbEntry.word, dbEntry))
      .toMap
  }

  def WriteDB(jdb: Database): Unit = {
    //DBFile.overwrite(jdb.asJson.noSpaces)
  }

  def searchKeyword(word: String): Option[Map[String, Int]] = {
    jdb.get(word).map(entry => entry.appearances)
  }


  // TODO : Properly deconvolute the two functions below.
  /**
    * Internal function to list keywords between start_val and end_val in the keyword DB.
    *
    * @param start_val the index of the keyword to start listing from
    * @param n_print   the number of keywords to return
    * @return
    */
  private def _listKeywords(start_val: Int, n_print: Int): Iterable[String] = {
    jdb.keys.slice(start_val, start_val + n_print)
  }

  /**
    * Prints out keywords fulfilling the options passed below in a pretty manner.
    *
    * @param options An OptionMap that may contain the keys 'n_start and 'n_view, representing the index
    *                of the key to start listing from and the number of keys to list, respectively.
    */
  def listKeywords(options: Map[String, Int]): Unit = {
    if (dbIsEmpty) {
      println("No keys to list."); sys.exit(0)
    }
    val start_val: Int = (options getOrElse('n_start, 0)).toString.toInt
    if (start_val > jdb.keys.size) {
      println("ERR: Start requested is higher than the # of entries."); sys.exit(1)
    }
    if (start_val < 0) {
      println("ERR: Start value must be >= 0."); sys.exit(1)
    }

    val user_friendly_start = start_val + 1

    var n_view: Int = (options getOrElse('n_view, 10)).toString.toInt + 1
    if (start_val + n_view > jdb.keys.size) {
      n_view = jdb.keys.size
      println("WARNING: More keys were requested than exist starting from key " + user_friendly_start)
    }

    println("Viewing keys from " + user_friendly_start + " to " + (start_val + n_view) + "...")
    println(_listKeywords(start_val, n_view)
      .fold("")(_ + ", " + _)
      .substring(2))

  }

  // TODO: How do I make this Null-Safe?
  def findNewKeywords(file: File): (String, List[(String, Int)]) = {
    val found = file.lines
      .toIterable.zipWithIndex.filter({
      case (line, _) => keywordReg.findFirstIn(line).isDefined
    }).toList

    (getNameFromPath(file), found)
  }

  def addNewKeywords(filePrefix: String, keywords: List[(String, Int)]): Unit = {
    keywords
      .map({ case (keyword: String, line: Int) =>
        (keyword, s"$filePrefix:$line")
      })
      .foreach({
        case (keyword: String, fileInfo: String) =>
          jdb(keyword) = jdb.getOrElse(keyword, Array.empty) + Array(fileInfo)
      })
  }
}
