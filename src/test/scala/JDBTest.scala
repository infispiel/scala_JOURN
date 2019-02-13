import better.files.File
import org.scalatest.FunSuite
import journalApp._

import scala.collection.mutable

class JDBTest extends FunSuite{
  val db_file_temp:File = File.newTemporaryFile("tmp","db")
  test("LoadDB_Empty_Test")
  {
    assert(JDB.LoadDB(db_file_temp).isEmpty)
  }

  test("LoadDB_Test")
  {
    db_file_temp.write("#KEYWORD;FILE:10,FILE2:3")
    assert(JDB.LoadDB(db_file_temp).getOrElse("KEYWORD", Array.empty).deep == Array("FILE:10","FILE2:3").deep)
  }

  lazy val db_temp:mutable.Map[String,Array[String]] = JDB.LoadDB(db_file_temp)
  test("searchForKeyword_DB_Test")
  {
    // manually set the file to load to be the temporary file we've created
    JDB.DBFile = db_file_temp
    assert(JDB.searchKeyword("KEYWORD").deep == Array("FILE:10","FILE2:3").deep)

  }
}
