package code6za.tech.prometheus

import PrometheusDataProtocol.jsonFormat
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, JsonFormat, deserializationError}

case class Metric (name: String,instance: String,job: String)
case class Data ( resultType: String, result: Seq[Result]          )
case class Result (        metric: Metric,                   values: Seq[Values]             )

case class PrometheusData ( status: String, data: Data )

case class Values (epoch: Double,value: String)
case class PrometheusReadings ( cpu: PrometheusData, container: PrometheusData )

case class SummaryReading (job: String, value: Double)
import spray.json._

case class JobMapping (kubernetesHost: String, prometheusJob: String)
object PrometheusDataProtocol extends DefaultJsonProtocol {


  implicit val MetricProtocol: JsonFormat[Metric] = new JsonFormat[Metric] {
    def read(json: JsValue): Metric = {
      json.asJsObject.getFields("__name__", "instance", "job") match {
        case Seq(JsString(name), JsString(instance), JsString(job)) => Metric(name, instance, job)
        case _ => deserializationError("Expected fields: 'name' (JSON string) and 'instance' (JSON string)")
      }
    }

    def write(obj: Metric): JsObject = JsObject("__name__" -> JsString(obj.name), "instance" -> JsString(obj.instance), "job" -> JsString(obj.job))
  }

  implicit val ValuesProtocol: JsonFormat[Values] = new JsonFormat[Values] {
    def read(json: JsValue): Values = {
      json match {
        case y: JsArray => Values(y.elements(0).convertTo[Double],y.elements(1).convertTo[String])
        case _ => deserializationError("Expected fields: 'name' (JSON string) and 'instance' (JSON string)")
        //  case List[JsNumber(epoch), JsString(value)] => Values(epoch.doubleValue, value)
      }

    }

    def write(obj: Values): JsArray = JsArray( JsNumber(obj.epoch),JsString(obj.value))

  }
  implicit val formatterJobMapping: RootJsonFormat[JobMapping] = jsonFormat(JobMapping, "kubernetesHost", "prometheusJob")
  implicit val formatterResult: RootJsonFormat[Result] = jsonFormat(Result, "metric", "values")
  implicit val formatterData: RootJsonFormat[Data] = jsonFormat(Data, "resultType", "result")
  implicit val formatterPrometheusData: RootJsonFormat[PrometheusData] = jsonFormat(PrometheusData, "status", "data")
}