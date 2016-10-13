package ind.co.pg

import java.lang.System._

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import ind.co.pg.Models.{Customer}
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by Admin on 12-10-2016.
  *
  */
trait RestService {
  implicit val system:ActorSystem
  implicit val materializer:ActorMaterializer

  private val logger = LoggerFactory.getLogger("RestService")
  lazy val testRoute: Route = {
    extractClientIP { ip =>
      logger.info(lineSeparator())
      logger.info("******************************************")
      logger.info(s"Request from $ip")

      get {
        path("ping") {
          complete("Hello World")
        } ~
          path("license") {
            complete("License :  L123")
          }
      }
    }
  }

  import ind.co.pg.Models.ServiceJsonProtoocol.customerProtocol


  lazy val purchaseRoute: Route = {
    extractClientIP { ip =>
      logger.info(lineSeparator())
      logger.info("******************************************")
      logger.info(s"Request from $ip")

      post {
        path("purchase") {
            entity(as[Customer]) {

              customer => prosessCustomer(customer)
            }
        }
      }

    }
  }

  def authinticateUser(customer: Customer): Future[Boolean] = {
    /*
    1) Verify the customer.
     */

    // create a Future
    val f = Future {
      sleep(500)
      true
    }
    f
  }

  def handleSuccessHttpResponseFromXchange(xchangeHttpResponse: HttpResponse, customer: Customer): Route = {

  }

  def handlePurchase(customer : Customer): server.Route = {

    onComplete(authinticateUser(customer)) {
      case Success(xchangeHttpResponse) =>
        handleSuccessHttpResponseFromXchange(xchangeHttpResponse, customer)
      case Failure(ex) =>
        logger.error(ex.getMessage, ex)
        complete(HttpResponse(status = InternalServerError, entity = s"Error fetching license: ${ex.getMessage}"))
    }
  }
  def prosessCustomer(customer : Customer): Route = {
    complete {
      logger.info(s"got customer with name ${customer.name}")
      s"got customer with name ${customer.name}"
    }
  }

}
