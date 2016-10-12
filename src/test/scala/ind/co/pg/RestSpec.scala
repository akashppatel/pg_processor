package ind.co.pg

/**
  * Created by LENOVO PC on 13-10-2016.
  */

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import ind.co.pg.Models.Customer
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model._

class RestSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService{
  "Customer API" should {
    "Posting to /customer should add the customer" in {

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


      /*postRequest ~> purchaseRoute ~>  check {
        status.isSuccess() shouldEqual true
      }*/
    }

  }
}