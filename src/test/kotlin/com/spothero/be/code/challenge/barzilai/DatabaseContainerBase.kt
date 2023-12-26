package com.spothero.be.code.challenge.barzilai

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer

@ContextConfiguration(initializers = [DatabaseContainerBase.Initializer::class])
abstract class DatabaseContainerBase {

    companion object {
        val postgreSQLContainer = PostgreSQLContainer("postgres:alpine").apply {
            withDatabaseName("spothero_rates")
            withUsername("admin")
            withPassword("1234")
        }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            postgreSQLContainer.start()

            TestPropertyValues.of(
                "spring.datasource.url=${postgreSQLContainer.jdbcUrl}",
                "spring.datasource.username=${postgreSQLContainer.username}",
                "spring.datasource.password=${postgreSQLContainer.password}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }
}