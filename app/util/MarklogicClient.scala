package util

import scala.concurrent.Future
import scala.xml.Elem

class MarklogicClient(httpClient: XmlHttpClient, marklogicUrl: String, credentials: Credentials) {

  def get(endpoint: String, parameters: Map[String, String] = Map.empty): Future[Elem] = {

    httpClient.get(
      url = marklogicUrl,
      credentials = credentials,
      parameter = transFormSpaceToAmpersand(parameters)
    )
  }

  private def transFormSpaceToAmpersand(parameters: Map[String, String]): Map[String,String] = {
    for( (k,v) <- parameters) yield (k,v.trim().replaceAll(" +", " ") )
  }

}
