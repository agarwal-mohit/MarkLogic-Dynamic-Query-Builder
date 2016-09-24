package controllers

import play.api.mvc.{Action, Controller}
import util.{MarklogicClient,JsonSerializer}
import util.Constants._
import util.XmlExtensions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Elem

case class SearchResult(Environment:String = "NA", MaterialGroup: String ="NA", MaterialFamily: String="NA", Material: String="NA", CRRate: String="NA", Duration: String="NA",Concentration: String="NA",PressureMin:String="NA",PressureMax:String="NA",TemperatureMin:String="NA", TemperatureMax:String="NA",Context: String ="Context")

case class SearchResults(results: List[SearchResult])

class SearchController(marklogicClient: MarklogicClient, serializer: JsonSerializer[SearchResults]) extends Controller {

  def search(searchTerm: String) = Action.async {
    val searchTermParameter = SearchTermParamName -> searchTerm.trim
    val queryName = searchXQueryName
    val searchParameters = Map(searchTermParameter)

    val response = marklogicClient.get(queryName, searchParameters).map(elem => transform(elem))
    response.map(dataset => Ok(serializer.serialize(dataset)))
  }

  def transform(elem: Elem): SearchResults = {
    val documents = elem \\ "Doc"

    val result = documents.map(doc => {
      val environment = (doc \ "Environment").getOnlyNode.text
      val materialGroup = (doc \ "MaterialGroup").getOnlyNode.text
      val materialFamily = (doc \ "MaterialFamily").getOnlyNode.text
      val material = (doc \ "Material").getOnlyNode.text
      val crRate = (doc \ "crRate").getOnlyNode.text
      val duration = (doc \ "Duration").getOnlyNode.text
      val concentration = (doc \ "Concentration").getOnlyNode.text
      val temperatureMin = (doc \ "TemperatureMin").getOnlyNode.text
      val temperatureMax = (doc \ "TemperatureMax").getOnlyNode.text
      val pressureMin = (doc \ "PressureMin").getOnlyNode.text
      val pressureMax = (doc \ "PressureMax").getOnlyNode.text
      SearchResult(environment, materialGroup, materialFamily, material, crRate, duration, concentration, pressureMin,pressureMax,temperatureMin,temperatureMax)
    }).toList
    new SearchResults(result)
  }

  def getContext(searchTerm: String) = Action.async {

    val searchTermParameter = SearchTermParamName -> searchTerm.trim
    val queryName = contextXQueryName
    val searchParameters = Map(searchTermParameter)
    val response = marklogicClient.get(queryName, searchParameters).map(elem => transformContext(elem))
    response.map(dataset => Ok(serializer.serialize(dataset)))
  }

  def transformContext(elem: Elem): SearchResults = {
    val contexts = elem \\ "name"

    val contextResult = contexts.map(context => {
      SearchResult(Context=context.getOnlyNode.text)
    }).toList
    new SearchResults(contextResult)
  }

}