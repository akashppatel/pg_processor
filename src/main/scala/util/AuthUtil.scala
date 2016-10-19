package util

import akka.http.scaladsl.server.directives.Credentials

/**
  * Created by Admin on 19-10-2016.
  */
object AuthUtil {
  def myUserPassAuthenticator(credentials: Credentials): Option[String] =
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(id)
      case _ => None
    }
}
