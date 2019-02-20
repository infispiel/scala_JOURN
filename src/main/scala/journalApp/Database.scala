package journalApp

case class Database(entries: Map[String, DatabaseEntry]) {
  val isEmpty:Boolean = entries.isEmpty
  val nEntries:Int = entries.size

  def addEntry(entry:DatabaseEntry): Database = {
    val newEntry:DatabaseEntry = entries.get(entry.word).map(_.addAppearances(entry)).getOrElse(entry)
    Database(entries ++ Map(entry.word -> newEntry))
  }

  def addEntry(entry:String): Database = {
    val newDatabaseEntry = DatabaseEntry(entry)
    addEntry(newDatabaseEntry)
  }

  def get(word:String): Option[DatabaseEntry] = {
    entries.get(word)
  }

  def slice(start:Int, stop:Int):Database = {
    Database(entries.slice(start, stop))
  }

  override def toString(): String = {
    entries.map(
      _.toString()
    )
      .fold("")(_ + "\n" + _).trim
  }

  def saveString():String = {
    entries.map(
      (entry:(String, DatabaseEntry)) => { entry._2.saveString() }
    )
    .fold("")(_ + "\n" + _).trim
  }

}

