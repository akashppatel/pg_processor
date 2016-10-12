package ind.co.pg

import Models.Customer
import net.liftweb.json.Serialization._
import net.liftweb.json.{NoTypeHints, Serialization}

/**
  * Created by Admin on 12-10-2016.
  */
case class PurchaseRequestResponse(customer: Customer) {
  implicit val formats = Serialization.formats(NoTypeHints)

  def toJson() = write(this)
}
