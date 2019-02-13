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

  test("searchForKeyword_DB_Test")
  {
    // manually set the file to load to be the temporary file we've created
    JDB.DBFile = db_file_temp
    assert(JDB.searchKeyword("KEYWORD").deep == Array("FILE:10","FILE2:3").deep)
  }

  test("AddKeyword_DB_Test")
  {
    JDB.addNewKeywords("FILE3",List(("KEYWORD2",10)))
    assert(JDB.searchKeyword(word="KEYWORD2").deep == Array("FILE3:10").deep)
  }

  test("Write_DB_Test")
  {
    JDB.WriteDB()
    assert(db_file_temp.contentAsString == "#KEYWORD;FILE:10,FILE2:3\n#KEYWORD2;FILE3:10")
  }
}
