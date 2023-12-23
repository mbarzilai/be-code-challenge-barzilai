package com.spothero.be.code.challenge.barzilai

import com.spothero.be.code.challenge.barzilai.filter.OverrideParametersEncodingFilter
import com.spothero.be.code.challenge.barzilai.model.Rate
import com.spothero.be.code.challenge.barzilai.repository.RateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.time.DayOfWeek

@Component
class ApplicationConfig {

    @Autowired
    lateinit var rateRepository: RateRepository

    @Autowired
    lateinit var overrideParametersEncodingFilter: OverrideParametersEncodingFilter

    @Bean
    fun commandLineRunner(): CommandLineRunner {
        return CommandLineRunner {
            val rate1 = Rate(
                setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
                "0900",
                "2100",
                "America/Chicago",
                1500
            )
            val rate2 = Rate(
                setOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                "0900",
                "2100",
                "America/Chicago",
                2000
            )
            val rate3 = Rate(
                setOf(DayOfWeek.WEDNESDAY),
                "0600",
                "1800",
                "America/Chicago",
                1750
            )
            val rate4 = Rate(
                setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY),
                "0100",
                "0500",
                "America/Chicago",
                1000
            )
            val rate5 = Rate(
                setOf(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY),
                "0100",
                "0700",
                "America/Chicago",
                925
            )
            rateRepository.saveAll(listOf(rate1, rate2, rate3, rate4, rate5))
        }
    }

    @Bean
    fun filterRegistration(): FilterRegistrationBean<OverrideParametersEncodingFilter> {
        val registrationBean = FilterRegistrationBean(overrideParametersEncodingFilter)
        registrationBean.addUrlPatterns("/price")
        return registrationBean
    }
}