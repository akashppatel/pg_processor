package ind.co.pg

import net.liftweb.json.Serialization._
import net.liftweb.json.{NoTypeHints, Serialization}

/**
  * Created by Admin on 12-10-2016.
  */

//import spray.json.DefaultJsonProtocol

object Models {
  case class Customer(name: String)
  object ServiceJsonProtoocol {
    implicit val customerProtocol = PurchaseRequestResponse(Customer)
  }

  case class PurchaseRequestResponse(customer: Customer) {
    implicit val formats = Serialization.formats(NoTypeHints)

    def toJson() = write(customer)
  }
}
