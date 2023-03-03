package code6za.tech.prometheus

import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import spray.json._

case class CollectorReading(metric:String, source:String, epoch: Double, value: String)

object PrometheusExportProtocol extends DefaultJsonProtocol {
  implicit val formatterData: RootJsonFormat[CollectorReading] = jsonFormat(CollectorReading, "metric", "source", "epoch", "value")
}