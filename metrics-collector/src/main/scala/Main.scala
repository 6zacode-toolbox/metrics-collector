import akka.event.slf4j.Logger
import code6za.tech.prometheus.{Collector, CollectorReading}

import scala.collection.mutable.ListBuffer

object Main {
  val logger = Logger(getClass.getName)

  def main(args: Array[String]): Unit = {
    logger.debug("Hello world!")

    val metrics = List("CPU_Temperature%5B10m%5D",
      "sensor_lm_temperature_celsius%5B10m%5D",
      "engine_daemon_container_states_containers%5B10m%5D",
      "engine_daemon_engine_cpus_cpus%5B10m%5D"
      ,"engine_daemon_engine_memory_bytes%5B10m%5D",
      "node_memory_HighFree_bytes%5B10m%5D",
      "node_filesystem_free_bytes%5B10m%5D")
    val collector = new Collector()
    var allMetrics: Map[String, ListBuffer[CollectorReading]] = collector.collectAndMergeMetrics(metrics)
    logger.debug(allMetrics.toString)
  }


}