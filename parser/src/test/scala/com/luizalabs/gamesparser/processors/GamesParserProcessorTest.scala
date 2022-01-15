package com.luizalabs.gamesparser.processors

import com.luizalabs.gamesparser.constants.Constants.GAMES_SEPARATOR
import com.luizalabs.gamesparser.models.{Game, ProcessInitializer}
import com.luizalabs.gamesparser.utils.{ApiUtils, FileUtils}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.FunSuite
import org.scalatestplus.junit.JUnitRunner
import org.scalatestplus.mockito.MockitoSugar

@RunWith(classOf[JUnitRunner])
class GamesParserProcessorTest extends FunSuite with MockitoSugar  {

  test("GamesParserProcessorTest.handleKillByWorldValue") {
    assert(GamesParserProcessor.handleKillByWorldValue(2).equals(2))
    assert(GamesParserProcessor.handleKillByWorldValue(1).equals(1))
    assert(GamesParserProcessor.handleKillByWorldValue(0).equals(0))
    assert(GamesParserProcessor.handleKillByWorldValue(-1).equals(0))
  }

  test("GamesParserProcessorTest.getKills") {

    val data: Array[String] = Array(
      "Item: 2 ammo_shells",
      "Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
      "ClientDisconnect: 2"
    )

    val kills = GamesParserProcessor.getKills(data)

    assert(kills.length.equals(1))
    assert(kills(0).equals("Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT"))
  }

  test("GamesParserProcessorTest.getPlayers") {

    val data: Array[String] = Array(
      "ClientUserinfoChanged: 4 n\\Zeh\\t\\0\\model\\sarge/default\\hmodel\\sarge/default\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl",
      "ClientUserinfoChanged: 3 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0",
      "Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
      "Item: 4 weapon_rocketlauncher",
      "Item: 4 ammo_rockets",
      "Kill: 1022 4 22: <world> killed Zeh by MOD_TRIGGER_HURT",
      "ClientUserinfoChanged: 2 n\\Dono da Bola\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0",
      "Item: 3 weapon_railgun",
      "Item: 2 weapon_rocketlauncher"
    )

    val players = GamesParserProcessor.getPlayers(data)

    assert(players.length.equals(3))
    assert(players.contains("Isgalamido"))
    assert(players.contains("Zeh"))
    assert(players.contains("Dono da Bola"))
  }

  test("GamesParserProcessorTest.getKillers") {

    val data: Array[String] = Array(
      "ClientUserinfoChanged: 4 n\\Zeh\\t\\0\\model\\sarge/default\\hmodel\\sarge/default\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl",
      "ClientUserinfoChanged: 3 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0",
      "Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
      "Item: 4 weapon_rocketlauncher",
      "Item: 4 ammo_rockets",
      "Kill: 1022 4 22: <world> killed Zeh by MOD_TRIGGER_HURT",
      "ClientUserinfoChanged: 2 n\\Dono da Bola\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0",
      "Item: 3 weapon_railgun",
      "Item: 2 weapon_rocketlauncher",
      "Item: 2 ammo_shells",
      "Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
      "ClientDisconnect: 2",
      "Kill: 3 2 10: Isgalamido killed Dono da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Isgalamido killed Zeh da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Zeh killed Dono da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Zeh killed Isgalamido by MOD_RAILGUN",
      "Kill: 3 2 10: Isgalamido killed Dono da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Isgalamido killed Zeh by MOD_RAILGUN"
    )

    val players = GamesParserProcessor.getPlayers(data)
    val killers = GamesParserProcessor.getKillers(GamesParserProcessor.getKills(data), players)

    assert(killers.length.equals(3))
    assert(killers(0).player.equals("Zeh"))
    assert(killers(0).kills.equals(1))
    assert(killers(1).player.equals("Isgalamido"))
    assert(killers(1).kills.equals(2))
    assert(killers(2).player.equals("Dono da Bola"))
    assert(killers(2).kills.equals(0))

  }

  test("GamesParserProcessorTest.parseGame") {

    val game: Row = Row(
      "  1:57 ClientUserinfoChanged: 4 n\\Zeh\\t\\0\\model\\sarge/default\\hmodel\\sarge/default\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\n" +
      "  1:58 ClientUserinfoChanged: 3 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\n" +
      "  1:59 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT\n" +
      "  1:59 Item: 4 weapon_rocketlauncher\n" +
      "  2:00 Item: 4 ammo_rockets\n" +
      "  2:01 Kill: 1022 4 23: <world> killed Zeh by MOD_TRIGGER_HURT\n" +
      "  2:02 ClientUserinfoChanged: 2 n\\Dono da Bola\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0\n" +
      "  2:03 Item: 3 weapon_railgun\n" +
      "  2:04 Item: 2 weapon_rocketlauncher\n" +
      "  2:05 Item: 2 ammo_shells\n" +
      "  2:06 Kill: 1022 2 24: <world> killed Isgalamido by MOD_TRIGGER_HURT\n" +
      "  2:07 ClientDisconnect: 2\n" +
      "  2:07 Kill: 3 2 10: Isgalamido killed Dono da Bola by MOD_RAILGUN\n" +
      "  2:10 Kill: 3 2 11: Isgalamido killed Zeh da Bola by MOD_RAILGUN\n" +
      "  2:14 Kill: 3 2 12: Zeh killed Dono da Bola by MOD_RAILGUN\n" +
      "  2:18 Kill: 3 2 13: Zeh killed Isgalamido by MOD_RAILGUN\n" +
      "  2:18 Kill: 3 2 14: Isgalamido killed Dono da Bola by MOD_RAILGUN\n" +
      "  2:60 Kill: 3 2 15: Isgalamido killed Zeh by MOD_RAILGUN\n" +
      "  2:06 Kill: 1022 2 24: <world> killed Isgalamido by MOD_TRIGGER_HURT\n"
    )

    val result = GamesParserProcessor.parseGames(game)

    assert(result.length.equals(13))

  }

  test("GamesParserProcessorTest.saveGame") {
    val testProcess = ProcessInitializer(
      null,
      "",
      mock[FileUtils],
      mock[ApiUtils]
    )

    when(testProcess.apiUtils.execute(any[Game])).thenReturn(true)

    val game = Array(
      "ClientUserinfoChanged: 4 n\\Zeh\\t\\0\\model\\sarge/default\\hmodel\\sarge/default\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl",
      "ClientUserinfoChanged: 3 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0",
      "Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
      "Item: 4 weapon_rocketlauncher",
      "Item: 4 ammo_rockets",
      "Kill: 1022 4 22: <world> killed Zeh by MOD_TRIGGER_HURT",
      "ClientUserinfoChanged: 2 n\\Dono da Bola\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0",
      "Item: 3 weapon_railgun",
      "Item: 2 weapon_rocketlauncher",
      "Item: 2 ammo_shells",
      "Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
      "ClientDisconnect: 2",
      "Kill: 3 2 10: Isgalamido killed Dono da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Isgalamido killed Zeh da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Zeh killed Dono da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Zeh killed Isgalamido by MOD_RAILGUN",
      "Kill: 3 2 10: Isgalamido killed Dono da Bola by MOD_RAILGUN",
      "Kill: 3 2 10: Isgalamido killed Zeh by MOD_RAILGUN"
    )

    GamesParserProcessor.saveGame(game, testProcess)

    verify(testProcess.apiUtils, times(1)).execute(any[Game])
  }

  test("GamesParserProcessorTest.process") {
    val config = new SparkConf()
      .setAppName("GamesParserTest")
      .setMaster("local[4]")

    val spark = SparkSession
      .builder
      .config(config)
      .getOrCreate()

    val testProcess = ProcessInitializer(spark, "src/test/resources/", mock[FileUtils], mock[ApiUtils])

    when(testProcess.fileUtils.getListOfLogFiles(any()))
      .thenReturn(List("src/test/resources/game.log"))
    when(testProcess.fileUtils.readFileToDataframe(any(), any()))
      .thenReturn(spark.read.option("lineSep", GAMES_SEPARATOR).text("src/test/resources/game.log"))

    GamesParserProcessor.process(testProcess)

    verify(testProcess.apiUtils, times(1)).execute(any[Game])

  }
}

