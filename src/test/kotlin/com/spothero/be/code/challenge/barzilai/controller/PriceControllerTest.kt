package com.spothero.be.code.challenge.barzilai.controller

import com.spothero.be.code.challenge.barzilai.dto.model.PriceResponse
import com.spothero.be.code.challenge.barzilai.exception.UnavailableException
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import com.spothero.be.code.challenge.barzilai.service.RateService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExtendWith(MockKExtension::class)
class PriceControllerTest {

    @MockK
    private lateinit var rateService: RateService

    @InjectMockKs
    private lateinit var priceController: PriceController

    companion object {
        val START: Instant = Instant.parse("2015-07-04T15:00:00+00:00")
        val END: Instant = Instant.parse("2015-07-04T20:00:00+00:00")
    }

    @Test
    fun testGetPrice() {
        val expectedResponse = PriceResponse(2000)

        every { rateService.getRatePriceByStartAndEnd(START, END) } returns 2000

        val response = priceController.getPrice(START, END)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResponse)
    }

    @Test
    fun testGetPrice_NoPriceFound() {
        coEvery { rateService.getRatePriceByStartAndEnd(START, END) } returns null

        assertThrows<UnavailableException> {
            priceController.getPrice(START, END)
        }
    }

    @Test
    fun testGetPrice_SameStartAndEndTime() {
        val exception = assertThrows<ValidationException> {
            priceController.getPrice(START, START)
        }

        verify(exactly = 0) { rateService.getRatePriceByStartAndEnd(any(), any()) }
        assertThat(exception.message).isEqualTo("Invalid request: End time must be after start time")
    }

    @Test
    fun testGetPrice_StartTimeAfterEndTime() {
        val earlierStart = START.minusSeconds(60)

        val exception = assertThrows<ValidationException> {
            priceController.getPrice(START, earlierStart)
        }

        verify(exactly = 0) { rateService.getRatePriceByStartAndEnd(any(), any()) }
        assertThat(exception.message).isEqualTo("Invalid request: End time must be after start time")
    }

    @Test
    fun testGetPrice_EndTimeExactlyOneDayAfterStart() {
        val startOneDayLater = START.plus(1, ChronoUnit.DAYS)

        assertThrows<UnavailableException> {
            priceController.getPrice(START, startOneDayLater)
        }

        verify(exactly = 0) { rateService.getRatePriceByStartAndEnd(any(), any()) }
    }

    @Test
    fun testGetPrice_EndTimeMoreThanOneDayAfterStart() {
        val startOneDayLater = START.plus(25, ChronoUnit.HOURS)

        assertThrows<UnavailableException> {
            priceController.getPrice(START, startOneDayLater)
        }

        verify(exactly = 0) { rateService.getRatePriceByStartAndEnd(any(), any()) }
    }

    @Test
    fun testHandleResponseUnavailableException() {
        val exception: ResponseEntity<String> = priceController.handleResponseUnavailableException(UnavailableException())

        assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(exception.body).isEqualTo("unavailable")
    }
}