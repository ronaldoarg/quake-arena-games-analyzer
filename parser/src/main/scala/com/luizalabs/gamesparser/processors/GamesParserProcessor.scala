package com.luizalabs.gamesparser.processors

import com.luizalabs.gamesparser.constants.Constants._
import com.luizalabs.gamesparser.models.{Game, GameKill, ProcessInitializer}
import org.apache.spark.sql.Row

object GamesParserProcessor {

  def process(process: ProcessInitializer) {
    val files = process.fileUtils.getListOfLogFiles(process.dir)

    files.foreach(processFile(_, process))
    files.foreach(process.fileUtils.renameFile)
  }

  def processFile(file: String, process: ProcessInitializer): Unit = {
    process.fileUtils.readFileToDataframe(file, process.spark)
      .collect()
      .map(parseGames)
      .filter(!_.isEmpty)
      .foreach(saveGame(_, process))
  }

  def parseGames(game: Row): Array[String] = {
    game
      .toString
      .replace("[", "")
      .split("\n")
      .map(line => line.slice(7, line.length()))
      .filter(line => line.startsWith(KILL) || line.startsWith(PLAYER_ENTER_ROOM))
  }

  def saveGame(game: Array[String], process: ProcessInitializer) {
    val kills = getKills(game)
    val players = getPlayers(game)

    process.apiUtils.execute(Game(kills.length, players, getKillers(kills, players)))
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
        val newValue = resultMap.apply(killed) - 1
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

  def handleKillByWorldValue(value: Int): Int = if (value < 0) 0 else value

}
