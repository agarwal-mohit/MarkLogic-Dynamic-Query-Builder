package config


import util.{Credentials, MarklogicClient, XmlHttpClient}
import controllers._
import util.Properties._

object Dependencies {
  private val xmlHttpClient = new XmlHttpClient

  val marklogicDocumentsClient = new MarklogicClient(xmlHttpClient, markLogicDocumentsUrl, Credentials(username, password))

  val ResutsJsonTransformer = new ResutsJsonTransformer

}
