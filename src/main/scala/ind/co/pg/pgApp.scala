package ind.co.pg

import java.util.concurrent.TimeUnit._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

/**
  * Created by LENOVO PC on 10-10-2016.
  */
object pgApp extends App with RestService{
  implicit val system = ActorSystem("bridge-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private val logger = LoggerFactory.getLogger(pgApp.getClass)

  logger.info("Starting pg processor...")


  val serverPort = 8080


  /*lazy val route: Route = {
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
      }~
      path("purchase") {
        post {
          entity(as[Customer]) {
            customer => complete {
              logger.info(s"got customer with name ${customer.name}")
              s"got customer with name ${customer.name}"
            }
          }
        }
      }

    }
  }*/

  Http().bindAndHandle(purchaseRoute, "localhost", serverPort)

  logger.info("... started on port {}", serverPort)

  scala.sys.addShutdownHook {
    logger.info("Shutting down ActorSystem...")
    system.terminate()
    Await.result(system.whenTerminated, new FiniteDuration(30, SECONDS))
    logger.info("ActorSystem shutdown")
  }
}
