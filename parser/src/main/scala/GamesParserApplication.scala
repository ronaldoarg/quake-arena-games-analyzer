package com.luizalabs.gamesparser

import com.luizalabs.gamesparser.models.ProcessInitializer
import com.luizalabs.gamesparser.processors.GamesParserProcessor
import com.luizalabs.gamesparser.utils.{ApiUtils, FileUtils}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object GamesParserApplication {

  def main(arguments: Array[String]): Unit = {

    if (arguments.length > 0) {
      GamesParserProcessor.process(
        ProcessInitializer(
          createSparkSession(),
          arguments(0),
          new FileUtils,
          new ApiUtils
        )
      )
    }

  }

  private def createSparkSession(): SparkSession = {

    val config = new SparkConf()
      .setAppName("GamesParser")
      .setMaster("local[4]")

    SparkSession
      .builder
      .config(config)
      .getOrCreate()
  }
}