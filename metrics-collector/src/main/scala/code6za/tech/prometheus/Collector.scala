package code6za.tech.prometheus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.DurationInt

class Collector {
  private val prometheusHost = scala.util.Properties.envOrElse("PROMETHEUS_HOST", "127.0.0.1:9090")

  import PrometheusDataProtocol._
  import spray.json._
  /**
   * Query State of Prometheus metrics, ie: "CPU_Temperature%5B10m%5D" that is the encoded version of "CPU_Temperature[10m]"
   *
   * @param query
   * @return
   */
  def readFromPrometheus(query: String): PrometheusData = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = s"http://$prometheusHost/api/v1/query?query=$query"
    )
    val responseFuture = Http().singleRequest(request)
    val timeout = 60000.millis
    val responseAsString = Await.result(
      responseFuture.flatMap(resp => Unmarshal(resp.entity).to[String]),
      timeout
    )

    val convertedData = responseAsString.parseJson.convertTo[PrometheusData]
    convertedData
  }

   def collectAndMergeMetrics(metrics: List[String]): Map[String, ListBuffer[CollectorReading]] = {

    var allMetrics: Map[String, ListBuffer[CollectorReading]] = Map()
    metrics.foreach(metric => {
      val resultCPU = readFromPrometheus(metric)
      var listOfReadings = new ListBuffer[CollectorReading]()
      resultCPU.data.result.foreach(result => {
        result.values.foreach(reading => {
          listOfReadings += new CollectorReading(result.metric.name, result.metric.job, reading.epoch, reading.value)
        })

      })
      allMetrics += (metric -> listOfReadings)
    })
    allMetrics
  }
}
