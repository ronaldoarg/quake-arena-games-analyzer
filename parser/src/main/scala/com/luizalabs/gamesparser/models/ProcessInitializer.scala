package com.luizalabs.gamesparser.models

import com.luizalabs.gamesparser.utils.FileUtils
import org.apache.spark.sql.SparkSession

case class ProcessInitializer(spark: SparkSession, dir: String, fileUtils: FileUtils) { }
