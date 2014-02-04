package dropbox4s.datastore.internal.http

/**
 * @author mao.instantlife at gmail.com
 */

import org.specs2.mutable._
import dropbox4s.datastore.auth.AccessToken
import dispatch._
import scala.concurrent.ExecutionException

class DatastoreApiRequestorTest extends Specification {
  val baseUrl = "https://api.dropbox.com/1/datastores"

  val testToken = AccessToken("test-token")

  def authHeaderValue(token: AccessToken) = s"Bearer ${token.token}"

  implicit class RichReq(req: Req) {
    def isDatastoresApi(endpoints: String, method: String, token: AccessToken) = {
      req.url must equalTo(s"${baseUrl}${endpoints}")
      req.toRequest.getMethod must equalTo(method)
      req.toRequest.getHeaders.get("Authorization").get(0) must equalTo(authHeaderValue(token))
    }
  }

  // datastores/get_or_create_datastore
  "GerOrCreateDatastoreRequestor#generateReq" should {
    "throw exception when both parameter is null" in {
      GetOrCreateDatastoreRequestor.generateReq(null, null) must throwA[IllegalArgumentException]
    }

    "throw exception when dsid parameter is empty string" in {
      GetOrCreateDatastoreRequestor.generateReq(testToken, "") must throwA[IllegalArgumentException]
    }

    "url that is set post parameter dsid and authorization header" in {
      val req = GetOrCreateDatastoreRequestor.generateReq(testToken, "test-datastore")

      req isDatastoresApi ("/get_or_create_datastore", "POST", testToken)
      req.toRequest.getParams.size() must equalTo(1)
      req.toRequest.getParams.get("dsid").get(0) must equalTo("test-datastore")
    }
  }

  "GerOrCreateDatastoreRequestor#apply" should {
    "throw exception when unauth request is failed" in {
      GetOrCreateDatastoreRequestor(testToken, "failed-request") must throwA[ExecutionException]
    }
  }

  // datastores/get_datastore
  "GerDatastoreRequestor#generateReq" should {
    "throw exception when both parameter is null" in {
      GetDatastoreRequestor.generateReq(null, null) must throwA[IllegalArgumentException]
    }

    "throw exception when dsid parameter is empty string" in {
      GetDatastoreRequestor.generateReq(testToken, "") must throwA[IllegalArgumentException]
    }

    "url that is set post parameter dsid and authorization header" in {
      val req = GetDatastoreRequestor.generateReq(testToken, "test-datastore")

      req isDatastoresApi ("/get_datastore", "POST", testToken)
      req.toRequest.getParams.size() must equalTo(1)
      req.toRequest.getParams.get("dsid").get(0) must equalTo("test-datastore")
    }
  }

  "GerDatastoreRequestor#apply" should {
    "throw exception when unauth request is failed" in {
      GetDatastoreRequestor(testToken, "failed-request") must throwA[ExecutionException]
    }
  }

  // datastores/delete_datastore
  "DeleteDatastoreRequestor#generateReq" should {
    "throw exception when both parameter is null" in {
      DeleteDatastoreRequestor.generateReq(null, null) must throwA[IllegalArgumentException]
    }

    "throw exception when dsid parameter is empty string" in {
      DeleteDatastoreRequestor.generateReq(testToken, "") must throwA[IllegalArgumentException]
    }

    "url that is set post parameter dsid and authorization header" in {
      val req = DeleteDatastoreRequestor.generateReq(testToken, "test-handle")

      req isDatastoresApi ("/delete_datastore", "POST", testToken)
      req.toRequest.getParams.size() must equalTo(1)
      req.toRequest.getParams.get("handle").get(0) must equalTo("test-handle")
    }
  }

  "DeleteDatastoreRequestor#apply" should {
    "throw exception when unauth request is failed" in {
      DeleteDatastoreRequestor(testToken, "failed-request") must throwA[ExecutionException]
    }
  }

  // datastore/list_datastores
  "ListDatastoresRequestor#generateReq" should {
    "throw exception when access token is null" in {
      ListDatastoresRequestor.generateReq(null, ()) must throwA[IllegalArgumentException]
    }

    "url that is set authorization header" in {
      val req = ListDatastoresRequestor.generateReq(testToken, ())

      req isDatastoresApi ("/list_datastores", "POST", testToken)
    }
  }

  "ListDatastoresRequestor#apply" should {
    "throw exception when unauth request is failed" in {
      ListDatastoresRequestor(testToken) must throwA[ExecutionException]
    }
  }

  // datastore/get_snapshot
  "GetSnapshotRequestor#generateReq" should {
    "throw exception when both parameter is null" in {
      new GetSnapshotRequestor[Dummy].generateReq(null, null) must throwA[IllegalArgumentException]
    }

    "throw exception when handle parameter is empty string" in {
      new GetSnapshotRequestor[Dummy].generateReq(testToken, "") must throwA[IllegalArgumentException]
    }

    "url that is set post parameter handle and authorization header" in {
      val req = new GetSnapshotRequestor[Dummy].generateReq(testToken, "test-handle")

      req isDatastoresApi ("/get_snapshot", "POST", testToken)
      req.toRequest.getParams.size() must equalTo(1)
      req.toRequest.getParams.get("handle").get(0) must equalTo("test-handle")
    }
  }

  // the follow test is specification test.
//  "RequestNotFound" should {
//    "throw exception not found url" in {
//      val notFound = new DatastoreApiRequestor[Unit, Unit] {
//        def apply(token: AccessToken, input: Unit = ()): Unit = executeReq(token, input)
//
//        private[dropbox4s] def generateReq(token: AccessToken, input: Unit = ()) = {
//          require(Option(token).isDefined)
//
//          baseUrl / "not_found" <:< authHeader(token)
//        }
//      }
//
//      notFound(testToken) must throwA[ExecutionException]
//    }
//  }
}

case class Dummy(dummykey: String)
