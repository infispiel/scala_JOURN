package journalApp

import better.files.File

import scala.util.matching.Regex
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

class Jdb(DBFile:File) {
  def LoadDB(): Database = {
    DBFile.createIfNotExists()
    //decode[Database](DBFile.contentAsString)
    Database(DBFile.lines
      .map(DatabaseEntry.apply)
      .map(dbEntry => (dbEntry.word, dbEntry))
      .toMap)
  }

  def WriteDB(jdb: Database): Unit = {
    DBFile.overwrite(jdb.saveString())
    //DBFile.overwrite(jdb.asJson.noSpaces)
  }

  def searchKeyword(jdb: Database, word: String): Option[Map[String, Int]] = {
    jdb.get(word).map(entry => entry.appearances)
  }

  /**
    * Prints out keywords fulfilling the options passed below in a pretty manner.
    *
    * @param options An OptionMap that may contain the keys 'n_start and 'n_view, representing the index
    *                of the key to start listing from and the number of keys to list, respectively.
    */
  def listKeywords(jdb:Database, options: Map[String, Int]): Unit = {
    if (jdb.isEmpty) {
      println("No keys to list."); sys.exit(0)
    }
    val start_val: Int = (options getOrElse("n_start", 0)).toString.toInt
    if (start_val > jdb.nEntries) {
      println("ERR: Start requested is higher than the # of entries."); sys.exit(1)
    }
    if (start_val < 0) {
      println("ERR: Start value must be >= 0."); sys.exit(1)
    }

    var n_view: Int = (options getOrElse("n_view", 10)).toString.toInt + 1
    if (start_val + n_view > jdb.nEntries) {
      n_view = jdb.nEntries
    }

    println(jdb.slice(start_val, n_view).toString())

  }

  // TODO: How do I make this Null-Safe?
  def findNewKeywords(jdb:Database, file: File): Database = {
    file.lines
      .toIterable.zipWithIndex
      .filter({
        case (line: String, _) => {
          line.startsWith("# K:")
        }
      })
      .map(
        ( lineInfo:(String, Int)) => {
          new DatabaseEntry(lineInfo._1.substring(5), file, lineInfo._2 + 1)
        }:DatabaseEntry
      )
      .foldLeft(jdb)(
      (prevDB:Database, entry:DatabaseEntry) => {
        prevDB.addEntry(entry)
      }
    )
  }

  def addNewKeywords(jdb:Database, filePrefix: String, keywords: List[(String, Int)]): Database = {
    keywords.map(
      (keyword:(String, Int)) => {
        new DatabaseEntry(keyword._1, filePrefix, keyword._2)
      }:DatabaseEntry
    )
    .foldLeft(jdb)(
      (prevDB:Database, entry:DatabaseEntry) => {
        prevDB.addEntry(entry)
      }
    )
  }
}
