package util

import play.api.Play
import Play.current

object Properties {
  lazy val markLogicDocumentsUrl = requireProperty("MARKLOGIC_DOCUMENTS_URL")
  lazy val username = requireProperty("MARKLOGIC_USERNAME")
  lazy val password = requireProperty("MARKLOGIC_PASSWORD")

  private[util] def requireProperty(propName: String):String = {
    sys.env.get(propName).orElse(Play.configuration.getString(propName)).get
  }

}
