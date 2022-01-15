package com.luizalabs.gamesparser.models

import com.luizalabs.gamesparser.utils.{ApiUtils, FileUtils}
import org.apache.spark.sql.SparkSession

case class ProcessInitializer(spark: SparkSession, dir: String, fileUtils: FileUtils, apiUtils: ApiUtils) { }
