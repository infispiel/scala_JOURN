import better.files.File
import org.scalatest.FunSuite
import journalApp._

class JdbTest extends FunSuite{
  val db_file_temp:File = File.newTemporaryFile("tmp","db")

  test("Create empty DB")
  {
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    assert(db.toString() == "")
  }

  test("Create DB & add a keyword")
  {
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    val db2 = jdb.addNewKeywords(db,"asdf",List(("Hello",6)))
    assert(db2.toString().equals("(Hello,asdf:6)"))
  }

  test("Add keyword twice")
  {
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    val db2 = jdb.addNewKeywords(db,"asdf",List(("Hello",6)))
    val db3 = jdb.addNewKeywords(db2, "asdf2",List(("Hello",6)))
    assert(db3.toString().equals("(Hello,asdf:6,asdf2:6)"))
  }

  test("Add two keywords")
  {
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    val db2 = jdb.addNewKeywords(db,"asdf",List(("Hello",6)))
    val db3 = jdb.addNewKeywords(db2, "asdf2",List(("Hello2",6)))
    assert(db3.toString().equals("(Hello,asdf:6)\n(Hello2,asdf2:6)"))
  }

  test("Check Save String w/ 1 Keyword")
  {
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    val db2 = jdb.addNewKeywords(db,"asdf",List(("Hello",6)))
    assert(db2.saveString().equals("Hello;asdf:6"))
  }

  test("Check Save String w/ 2 Keywords")
  {
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    val db2 = jdb.addNewKeywords(db,"asdf",List(("Hello",6)))
    val db3 = jdb.addNewKeywords(db2, "asdf2",List(("Hello2",6)))
    assert(db3.saveString().equals("Hello;asdf:6\nHello2;asdf2:6"))
  }

  test("Check loading of 1 Keyword")
  {
    val db_file_1_keyword = db_file_temp.write("Hello;asdf:6")
    val jdb:Jdb = new Jdb(db_file_1_keyword)
    val db = jdb.LoadDB()
    assert(db.toString().equals("(Hello,asdf:6)"))
  }

  test("Check loading of 2 Keywords")
  {
    val db_file_1_keyword = db_file_temp.write("Hello;asdf:6")
    val db_file_2_keywords = db_file_1_keyword.append("\nHello2;asdf2:6")
    val jdb:Jdb = new Jdb(db_file_2_keywords)
    val db = jdb.LoadDB()
    assert(db.toString().equals("(Hello,asdf:6)\n(Hello2,asdf2:6)"))
  }

  test("Check loading of 1 Keyword multiple times")
  {
    val db_file_1_keyword = db_file_temp.write("Hello;asdf:6,asdf2:6")
    val jdb:Jdb = new Jdb(db_file_1_keyword)
    val db = jdb.LoadDB()
    assert(db.toString().equals("(Hello,asdf:6,asdf2:6)"))
  }

  test("Test Finding 1 new word in a file")
  {
    db_file_temp.write("")
    val file_text = "# K: word"
    val temporary_file = File.newTemporaryFile()
    temporary_file.write(file_text)
    val jdb:Jdb = new Jdb(db_file_temp)
    val db = jdb.LoadDB()
    val db2 = jdb.findNewKeywords(db, temporary_file)
    assert(db2.toString().equals("(word," + temporary_file.nameWithoutExtension + ":1)"))
  }
}
