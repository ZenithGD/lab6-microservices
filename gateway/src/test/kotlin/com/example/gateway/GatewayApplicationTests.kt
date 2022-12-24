package com.example.gateway

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpRequestTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `request to gateway slash registration should route to Eureka`() {
        val response = restTemplate.getForEntity("http://localhost:$port/registration/", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Instances currently registered with Eureka")
    }

    @Test
    fun `request to gateway by default should route to the web service`() {
        val response = restTemplate.getForEntity("http://localhost:$port/", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Microservices Demo - Web Server")
    }

    @Test
    fun `request to accounts search by ID should route to the web service`() {
        val response = restTemplate.getForEntity("http://localhost:$port/accounts/123456789", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Account Details")
    }

    @Test
    fun `request to accounts search by name should route to the web service`() {
        val response = restTemplate.getForEntity("http://localhost:$port/accounts/owner/Keri", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Accounts by Name")
    }

    @Test
    fun `request to accounts search page should route to the web service`() {
        val response = restTemplate.getForEntity("http://localhost:$port/accounts/search", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("Enter an account number or search for an account owner")
    }
}