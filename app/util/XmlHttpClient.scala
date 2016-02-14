package util

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.{XML, Elem}
import play.api.libs.ws._
import ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory
import play.api.Play.current


case class Credentials(username: String, password: String)

class XmlHttpClient {
  private val logger = LoggerFactory.getLogger(this.getClass)

  private val authScheme = WSAuthScheme.BASIC

  def get(url: String, credentials: Credentials, parameter: Map[String, String]): Future[Elem] = {
    WS.url(url).withQueryString(parameter.toList: _*)
      .withAuth(credentials.username, credentials.password, authScheme)
      .get()
      .map(response => handleResponse(response))
  }

  private def handleResponse(resp: WSResponse): Elem = {
    resp.status match {
      case 200 => XML.loadString(resp.body.trim)
      case _ =>
        logger.error(resp.body)
        throw new Exception(s"Response not OK. Http Status code:${resp.status}")
    }
  }

}

