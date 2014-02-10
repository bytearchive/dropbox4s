package dropbox4s.datastore.models

/**
 * @author mao.instantlife at gmail.com
 */

import org.specs2.mutable._
import dropbox4s.datastore.model.{TableRow, Table}
import org.json4s.JsonAST.{JField, JString, JArray, JValue}
import org.json4s.JsonDSL._
import dropbox4s.commons.DropboxException

class TableTest extends Specification {
  case class TestValue(key1: String, key2: Int, key3: List[String], key4: Option[String])
  val converter: (TestValue) => JValue = {value =>
    ("key1" -> value.key1) ~ ("key2" -> value.key2) ~ ("key3" -> value.key3) ~ ("key4" -> value.key4)
  }

  val testTableRows = List(
    TableRow("row-id-1", TestValue("value1", 0, List.empty, None)),
    TableRow("row-id-2", TestValue("value1-2", 1, List.empty, Some("value4-2")))
  )

  val testTable = Table("test-handle", "test-table", 0, converter, testTableRows)

  "rowDiff" should {
    "throw exception with null other value" in {
      testTable.rowDiff("row-id-1", null) must throwA[IllegalArgumentException]
    }

    "throw exception with null row id" in {
      testTable.rowDiff(null, TestValue("value1", 0, List("value1"), None)) must throwA[IllegalArgumentException]
    }

    "throw exception with not exists row-id" in {
      testTable.rowDiff("row-not-found", TestValue("value1", 0, List("value1"), None)) must
        throwA[DropboxException]
    }

    "returns field operation in change atom values" in {
      testTable.rowDiff("row-id-1", TestValue("value2", 0, List.empty, Some("value4"))) must
        contain(
          JField("key1", JArray(List(JString("P"), JString("value2")))),
          JField("key4", JArray(List(JString("P"), JString("value4"))))
        )
    }

    "returns field operation in delete atom values" in {
      testTable.rowDiff("row-id-2", TestValue("value1-2", 1, List.empty, None)) must
        contain(
          JField("key4", JArray(List(JString("D"))))
        )
    }

    "returns field operation in add list value" in {
      testTable.rowDiff("row-id-1", TestValue("value1", 0, List("value1", "value2"), None)) must
        contain(
          JField("key3", JArray(List(JString("P"), 0, JArray(List(JString("value1"), JString("value2"))))))
        )
    }
  }
}
