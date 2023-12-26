package com.spothero.be.code.challenge.barzilai.controller

import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.dto.validation.RatesValidator
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import com.spothero.be.code.challenge.barzilai.service.RateService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
class RatesControllerTest {

    @MockK
    private lateinit var rateService: RateService

    @MockK
    private lateinit var ratesValidator: RatesValidator

    @InjectMockKs
    private lateinit var ratesController: RatesController

    private val rate1 = RateDTO("mon,tues", "0900-1700", "America/New_York", 1200)
    private val rate2 = RateDTO("sat,sun", "0500-2100", "America/Chicago", 1000)

    @Test
    fun testGetRates() {
        val expectedResponse = Rates(listOf(rate1, rate2))
        every { rateService.getAllRates() } returns listOf(rate1, rate2)

        val response = ratesController.getRates()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResponse)
        verify(exactly = 1) { rateService.getAllRates() }
    }

    @Test
    fun testUpdateRates() {
        val ratesDTO = Rates(listOf(rate1, rate2))
        justRun { ratesValidator.validateRates(ratesDTO) }
        every { rateService.updateAllRates(ratesDTO.rates) } returns ratesDTO.rates

        val response = ratesController.updateRates(ratesDTO)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(ratesDTO)
        verify(exactly = 1) { ratesValidator.validateRates(ratesDTO) }
        verify(exactly = 1) { rateService.updateAllRates(ratesDTO.rates) }
    }

    @Test
    fun testUpdateRates_ValidationError() {
        val invalidRate = RateDTO("Monday", "9:00-12:00", "Chicago", 1200)
        val ratesDTO = Rates(listOf(invalidRate))
        every { ratesValidator.validateRates(ratesDTO) } throws ValidationException("this rate is invalid")

        val exception = assertThrows<ValidationException> { ratesController.updateRates(ratesDTO) }

        assertThat(exception.message).isEqualTo("Invalid request: this rate is invalid")
        verify(exactly = 0) { rateService.updateAllRates(any()) }
    }
}