package journalApp

object DatabaseEntry {

  def apply(line: String): DatabaseEntry = {
    val stripped = if (line.startsWith("#")) {
      line.substring(1, line.length)
    } else {
      line
    }
    val splitted: Array[String] = stripped.split(";")
    val word: String = splitted(0)
    val rawEntries: String = splitted(1)
    // rawEntries = DATE:5,DATE:10,DATE:12
    val splittedEntries = rawEntries.split(",")
    val entries: Map[String, Int] = splittedEntries
      .map(entry => entry.split(":"))
      .map(s => (s(0), Integer.parseInt(s(1))))
      .toMap
    DatabaseEntry(word, entries)
  }
}
case class DatabaseEntry(word: String, appearances: Map[String, Int]) {

  def this(word:String, file:String, line:Int) = {
    this(word, Map(file -> line))
  }

  def addAppearance(appearance: (String, Int)): DatabaseEntry = {
    DatabaseEntry(word, appearances + appearance)
  }

  def addAppearances(otherEntry: DatabaseEntry): DatabaseEntry = {
    if (!word.equals(otherEntry.word)) { throw new IllegalArgumentException("Two entries of different words were attempted to be merged.") }
    // TODO: What if a keyword appears multiple times in a file?
    DatabaseEntry(word, appearances ++ otherEntry.appearances)
  }

  override def toString():String = {
    appearances.map(
        (entry:(String,Int)) => {
          entry._1 + ":" + entry._2.toString
        }:String
      )
      .fold("")(_ + "," + _)
      .substring(1)
  }

}
