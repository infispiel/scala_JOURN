package journalApp

case class Database(entries: Map[String, DatabaseEntry]) {

  def addEntry(entry:DatabaseEntry): Database = {
    val newEntry:Map[String, DatabaseEntry] = Map(entry.word -> entry)
    Database(entries ++ newEntry)
  }

  def addEntry(entry:String): Database = {
    val newDatabaseEntry = DatabaseEntry(entry)
    addEntry(newDatabaseEntry)
  }

  def get(word:String): Option[DatabaseEntry] = {
    entries.get(word)
  }

}

