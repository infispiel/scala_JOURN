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
}
