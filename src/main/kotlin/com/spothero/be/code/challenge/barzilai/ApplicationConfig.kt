package com.spothero.be.code.challenge.barzilai

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.spothero.be.code.challenge.barzilai.dto.mapper.RateMapper
import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.filter.OverrideParametersEncodingFilter
import com.spothero.be.code.challenge.barzilai.service.RateService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ApplicationConfig {

    @Bean
    fun seedInitialData(rateMapper: RateMapper, rateService: RateService): CommandLineRunner {
        return CommandLineRunner {
            val mapper = jacksonObjectMapper()
            val inputStream = javaClass.getResourceAsStream("/rates_to_seed.json")
            try {
                val ratesFile = mapper.readValue(inputStream, Rates::class.java)
                rateService.updateAllRates(ratesFile.rates)
            } catch (e: Exception) {
                println("Unable to seed rates into database ${e.stackTraceToString()}")
            }
        }
    }

    @Bean
    fun filterRegistration(filter: OverrideParametersEncodingFilter): FilterRegistrationBean<OverrideParametersEncodingFilter> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.addUrlPatterns("/price")
        return registrationBean
    }
}