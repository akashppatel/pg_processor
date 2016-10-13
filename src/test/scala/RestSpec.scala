/**
  * Created by Admin on 12-10-2016.
  */


import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import ind.co.pg.RestService
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model._
// http://blog.madhukaraphatak.com/akka-http-testing/

class RestSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService{
  "PG API" should {
    "Posting to /purchase should done for customer" in {

      val jsonRequest = ByteString(
        s"""
           |{
           |    "name":"test"
           |}
        """.stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/purchase",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))


      postRequest ~> purchaseRoute ~> check {
        status.isSuccess() shouldEqual true
      }
    }

  }
}