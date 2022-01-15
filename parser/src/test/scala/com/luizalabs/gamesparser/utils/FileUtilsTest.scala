package com.luizalabs.gamesparser.utils

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatestplus.junit.JUnitRunner
import org.scalatestplus.mockito.MockitoSugar

@RunWith(classOf[JUnitRunner])
class FileUtilsTest extends FunSuite with MockitoSugar  {

  val fileUtils: FileUtils = new FileUtils

  test("GamesParserProcessorTest.getListOfLogFiles") {
    assert(fileUtils.getListOfLogFiles("src/test/resources/").length.equals(1))
    assert(fileUtils.getListOfLogFiles("src/test/resources/").head.contains("game.log"))
  }


}

