package util

import play.api.libs.json.JsValue

trait JsonDeserializer[A] {
  def deSerialize(json: JsValue): A
}
