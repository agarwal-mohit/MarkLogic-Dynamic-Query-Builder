package util

import play.api.libs.json.JsValue

trait JsonSerializer[A] {
  def serialize(value: A): JsValue
}
