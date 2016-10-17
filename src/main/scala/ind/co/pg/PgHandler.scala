package ind.co.pg

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import ind.co.pg.Models.Customer
import org.slf4j.LoggerFactory
import util.AppUtil

import scala.concurrent.Future

/**
  * Created by Admin on 17-10-2016.
  */
class PgHandler(private val pgName: String) {

  private val logger = LoggerFactory.getLogger("PgHandler")
  private val pgUrl = "www." + AppUtil.showStringOption(Option(this.pgName))+ ".com/purchase"

  def getPgName(): String =  pgName

  def getPgUrl(): String = pgUrl

  override def toString: String =
    "(" + this.pgName + ", " + pgUrl + ")"
}