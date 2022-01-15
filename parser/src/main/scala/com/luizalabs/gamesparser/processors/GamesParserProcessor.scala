package com.luizalabs.gamesparser.processors

import com.luizalabs.gamesparser.constants.Constants._
import com.luizalabs.gamesparser.models.{Game, GameKill, ProcessInitializer}
import com.luizalabs.gamesparser.utils.{ApiUtils, FileUtils}
import org.apache.spark.sql.{Row, SparkSession}

object GamesParserProcessor {

  def process(process: ProcessInitializer) {
    val files = process.fileUtils.getListOfLogFiles(process.dir)

    files.foreach(processFile(_, process.spark, process.fileUtils))

    files.foreach(process.fileUtils.renameFile(_))
  }

  def processFile(file: String, spark: SparkSession, fileUtils: FileUtils): Unit = {
    fileUtils.readFileToDataframe(file, spark)
      .collect()
      .map(parseGames)
      .filter(!_.isEmpty)
      .foreach(saveGame)
  }

  private def parseGames(game: Row): Array[String] = {
    game
      .toString
      .split("\n")
      .map(line => line.slice(7, line.length()))
      .filter(line => line.startsWith(KILL) || line.startsWith(PLAYER_ENTER_ROOM))
  }

  private def saveGame(game: Array[String]) {
    val kills = getKills(game)
    val players = getPlayers(game)

    ApiUtils.execute(Game(kills.length, players, getKillers(kills, players)))
  }

  def getPlayers(game: Array[String]): Array[String] = {
    game
      .filter(_.startsWith("ClientUserinfoChanged:"))
      .map(_.toString
        .split(" n")(1)
        .split("model")(0)
      ).map(item => item.slice(1, item.length - 5))
      .distinct
  }

  def getKills(game: Array[String]): Array[String] = game.filter(_.startsWith(KILL))

  def getKillers(kills: Array[String], players: Array[String]): Array[GameKill] = {
    var resultMap = Map.empty[String, Int]
    var result: Array[GameKill] = Array.empty

    players.foreach(player => {
      resultMap = resultMap + (player -> 0)
    })

    kills.foreach(kill => {
      val info = kill.split(": ")(2).split(" killed ")
      val killer = info(0)
      val killed = info(1).split(" by ")(0)

      if (killer.equals(WORLD_PLAYER)) {
        val newValue = handleKillByWorldValue(resultMap.apply(killed))
        resultMap = resultMap + (killed -> newValue)
      } else {
        val newValue = resultMap.apply(killer) + 1
        resultMap = resultMap + (killer -> newValue)
      }
    })

    players.foreach(player => {
      result = result :+ GameKill(player, resultMap.apply(player))
    })

    result
  }

  def handleKillByWorldValue(value: Int): Int = if (value > 0) value - 1  else 0

}
