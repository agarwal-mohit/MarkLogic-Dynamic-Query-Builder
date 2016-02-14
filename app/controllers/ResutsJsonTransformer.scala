package controllers

import util._
import play.api.libs.json._

class ResutsJsonTransformer extends JsonSerializer[SearchResults] with JsonDeserializer[SearchResults] {

  implicit val searchResult = Json.format[SearchResult]
  implicit val results = Json.format[SearchResults]

  def serialize(searchResults: SearchResults) = Json.toJson(searchResults)

  def deSerialize(json: JsValue) = {
    json.validate[SearchResults] match {
      case rs:JsSuccess[SearchResults] => rs.get
      case error: JsError => throw new RuntimeException(error.toString)
    }
  }
}