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
case class DatabaseEntry(word: String, appearences: Map[String, Int]) {

  def addEntry(appearence: (String, Int)): DatabaseEntry = {
    DatabaseEntry(word, appearences + appearence)
  }
}
