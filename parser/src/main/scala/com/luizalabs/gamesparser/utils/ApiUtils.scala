package com.luizalabs.gamesparser.utils

import com.google.gson.Gson
import com.luizalabs.gamesparser.constants.Constants._
import com.luizalabs.gamesparser.models.Game
import org.apache.http.HttpEntity
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils

class ApiUtils {

  implicit lazy val httpClient: CloseableHttpClient = HttpClients.createDefault()

  def execute(body: Game): Boolean = {

      val json = new Gson().toJson(body)
      val entity = new StringEntity(json, ContentType.APPLICATION_JSON)
      val request: HttpPost  = new HttpPost(API_URL)

      request.setEntity(entity)

      val response: CloseableHttpResponse = httpClient.execute(request)

      try {
        val entity: HttpEntity = response.getEntity
        EntityUtils.consume(entity);
      } finally {
        response.close()
      }

      response.getStatusLine.equals(201)
  }
}


