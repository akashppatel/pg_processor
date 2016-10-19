package ind.co.pg

import java.lang.System._

import _root_.util.{AuthUtil, AppUtil}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import akka.util.CompactByteString
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
  lazy val purchaseRoute: Route =  Route.seal {
    extractClientIP { ip =>
      logger.info(lineSeparator())
      logger.info("******************************************")
      logger.info(s"Request from $ip")

      post {
          pathPrefix("pg-api"){
            path("purchase") {
              entity(as[Customer]) {

                customer => handleClientSpecificPurchase(customer)
              }
            }~
            path("secured") {
              authenticateBasic(realm = "secure site", AuthUtil.myUserPassAuthenticator) { userName =>
                complete(s"The user is '$userName'")
              }
            }
        }
      }
    }
  }

  /**
    * Assembling client specfic calls to pg.
    * @param customer
    * @return
    */
  def handleClientSpecificPurchase(customer : Customer): server.Route = {

    val pgHandler : PgHandler = identifyClientsPg(customer)
    val clientRequest : HttpRequest = createClientRequestForIdentifiedPg(customer, pgHandler)
    //handleClientTransactionWithPg(customer,pgHandler,clientRequest)

    onComplete(handleClientTransactionWithPg(customer,pgHandler,clientRequest)) {
      case Success(pgHttpResponse) =>
        handleSuccessHttpResponseFromPg(pgHttpResponse, customer)
      case Failure(ex) =>
        logger.error(ex.getMessage, ex)
        complete(HttpResponse(status = InternalServerError, entity = s"Error fetching license: ${ex.getMessage}"))
    }
  }

  /**
    * Does actually call to pg.
    * @param customer
    * @param pgHandler
    * @param clientPgRequest
    * @return
    */
  def handleClientTransactionWithPg(customer : Customer, pgHandler : PgHandler, clientPgRequest : HttpRequest) : Future[HttpResponse] = {
    logger.info(s"Calling PG in progress .................. ")
    //Http().singleRequest(clientPgRequest)
    // create a Future
    val f = Future {
      //AppUtil.sleep(500)
      HttpResponse(200)
    }
    f
  }

  /**
    * Handles the response from the PG :
    * @param pgHttpResponse
    * @param customer
    * @return
    */
  def handleSuccessHttpResponseFromPg(pgHttpResponse:  HttpResponse, customer: Customer): Route = {
    complete {
      val customerName = AppUtil.capitalStartingLetterOfEachWord(customer.name);
      logger.info(s"got customer's payment response : ${customer.name}")
      logger.info("******************************************")
      s"${customerName} Thankyou for shopping."
      HttpResponse(200, entity = s"${customerName} Thankyou for shopping.")
    }
  }


  /**
    * Identify customers pg based on his information.
    * @param customer
    * @return
    */
  def identifyClientsPg(customer : Customer) : PgHandler = {
    val pgName = "payMoney"

    val pgHandler = new PgHandler(pgName)
    logger.info(s"customer name : ${customer.name} and Pg name identified is : ${pgName}")
    pgHandler
  }


  /**
    * After identifying clients pg, create pg specific request
    * @param customer
    * @param pgHandler
    * @return HttpRequest
    */
  def createClientRequestForIdentifiedPg(customer : Customer, pgHandler : PgHandler) : HttpRequest = {
    logger.info(s"creating PG [${pgHandler.getPgName()}] specific request : ${pgHandler.getPgUrl()} ")

    HttpRequest(
    method = HttpMethods.POST,
    uri = pgHandler.getPgUrl(),
    entity = HttpEntity(
    ContentTypes.`text/xml(UTF-8)`,
    CompactByteString(" client request entity hear")))
  }


  def buildPgRequest(customer : Customer, pgHandler : PgHandler): PgHandler = {
    logger.info(s"building request for pg : ${pgHandler.getPgName}")
    pgHandler
  }

}
