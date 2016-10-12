package ind.co.pg

import java.lang.System._

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import ind.co.pg.Models.{Customer}
import akka.http.scaladsl.marshalling.ToResponseMarshallable

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

  import ind.co.pg.Models.ServiceJsonProtoocol.customerProtocol._
  lazy val purchaseRoute: Route = {
    extractClientIP { ip =>
      logger.info(lineSeparator())
      logger.info("******************************************")
      logger.info(s"Request from $ip")

      post {
        path("purchase") {
            entity(as[Customer]) {
              customer => complete {
                logger.info(s"got customer with name ${customer.name}")
                s"got customer with name ${customer.name}"
              }
            }
        }
      }

    }
  }
}
