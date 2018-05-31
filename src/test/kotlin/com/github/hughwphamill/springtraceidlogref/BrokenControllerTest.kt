package com.github.hughwphamill.springtraceidlogref

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.VndErrors
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrokenControllerTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Test
    fun logref_exists() {
        // Given
        val uri = "/broken"

        // When
        val logref = rest.getForEntity(URI(uri), VndErrors.VndError::class.java).body?.logref

        // Then
        println("logref = $logref")
        assertThat(logref).isNotBlank()
    }
}