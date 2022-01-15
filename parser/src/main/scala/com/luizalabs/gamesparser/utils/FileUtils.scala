package com.luizalabs.gamesparser.utils

import java.io.File

import com.luizalabs.gamesparser.constants.Constants.{GAMES_SEPARATOR, LOG_FILE_EXTENSION, ALREADY_LOGGED_FILE_EXTENSION}
import org.apache.spark.sql.{DataFrame, SparkSession}

class FileUtils {

  def readFileToDataframe(file: String, spark: SparkSession): DataFrame = spark.read.option("lineSep", GAMES_SEPARATOR).text(file)

  def getListOfLogFiles(dir: String): List[String] = {
    val d = new File(dir)

    if (d.exists && d.isDirectory) {
      d.listFiles
        .filter(_.isFile)
        .map(_.getAbsolutePath)
        .filter(_.endsWith(LOG_FILE_EXTENSION))
        .toList
    } else {
      List[String]()
    }
  }

  def renameFile(file: String): Unit = new File(file).renameTo(new File(file.concat(ALREADY_LOGGED_FILE_EXTENSION)))

}
