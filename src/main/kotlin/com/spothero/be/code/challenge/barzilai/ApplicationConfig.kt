package com.spothero.be.code.challenge.barzilai

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.spothero.be.code.challenge.barzilai.dto.mapper.RateMapper
import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.dto.validation.RatesValidator
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import com.spothero.be.code.challenge.barzilai.filter.OverrideParametersEncodingFilter
import com.spothero.be.code.challenge.barzilai.service.RateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Component
class ApplicationConfig {
    
    val log: Logger = LoggerFactory.getLogger(ApplicationConfig::class.java)
    @Bean
    fun seedInitialData(rateMapper: RateMapper, ratesValidator: RatesValidator, rateService: RateService): CommandLineRunner {
        return CommandLineRunner {
            val mapper = jacksonObjectMapper()
            val inputStream = javaClass.getResourceAsStream("/rates_to_seed.json")
            try {
                val ratesFile = mapper.readValue(inputStream, Rates::class.java)
                ratesValidator.validateRates(ratesFile)
                rateService.updateAllRates(ratesFile.rates)
            } catch (e: ValidationException) {
                log.error("Unable to seed rates into database because invalid rate supplied in initial file: ${e.message}")
            } catch (e: Exception) {
                log.error("Unable to seed rates into database", e)
            }
        }
    }

    @Bean
    fun filterRegistration(filter: OverrideParametersEncodingFilter): FilterRegistrationBean<OverrideParametersEncodingFilter> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.addUrlPatterns("/price")
        return registrationBean
    }

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        return filter
    }
}