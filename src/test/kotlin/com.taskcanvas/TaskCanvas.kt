package com.taskcanvas

import com.thoughtworks.gauge.Step
import org.assertj.core.api.Assertions.assertThat
import java.io.FileInputStream
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URI
import java.util.Properties

class TaskCanvas {
    private val baseUrl = readBaseUrl()
    private val client = HttpClient.newHttpClient()
    private lateinit var response: HttpResponse<String>

    @Step("v1/systems/pingにリクエストを送るとpongが返ってくる")
    fun pingPong() {
        println("pong")
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/v1/systems/ping"))
            .GET()
            .build()

        response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertThat( response.body() ).isEqualTo("pong")
    }

    @Step("ステータスコードが<status>である")
    fun statusCode(status: Int) {
        assertThat(response.statusCode()).isEqualTo(status)
    }

    @Step("/v1/signUpにリクエストを送るとユーザー登録ができる")
    fun signUp() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/v1/signUp"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"password\":\"test\",\"email\": \"test@example.com\"}"))
    }

    private fun readBaseUrl(): String {
       val  properties = Properties()
       val propertiesFile = "src/test/resources/gauge.properties"
        FileInputStream(propertiesFile).use { properties.load(it) }
        println(properties.getProperty("rest.baseUrl"))
        return properties.getProperty("rest.baseUrl")
    }
}