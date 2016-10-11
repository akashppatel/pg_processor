import java.lang.System._
import java.util.concurrent.TimeUnit._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
/**
  * Created by LENOVO PC on 10-10-2016.
  */
object pgApp extends App {
  implicit val system = ActorSystem("bridge-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private val logger = LoggerFactory.getLogger(pgApp.getClass)

  logger.info("Starting pg processor...")


  val serverPort = 8080


  lazy val route: Route = {
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

  Http().bindAndHandle(pgApp.route, "localhost", serverPort)

  logger.info("... started on port {}", serverPort)

  scala.sys.addShutdownHook {
    logger.info("Shutting down ActorSystem...")
    system.terminate()
    Await.result(system.whenTerminated, new FiniteDuration(30, SECONDS))
    logger.info("ActorSystem shutdown")
  }
}
