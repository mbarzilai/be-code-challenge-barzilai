package com.spothero.be.code.challenge.barzilai.dto.validation

import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RatesValidatorTest {

    private val ratesValidator = RatesValidator()

    @Test
    fun testValidateRatesSuccess() {
        val rate1 = RateDTO("mon,tues,wed,thurs", "0000-2359", "UTC", 1200)
        val rate2 = RateDTO("fri,sat,sun", "0900-2100", "America/Chicago", 950)

        ratesValidator.validateRates(Rates(listOf(rate1, rate2)))
    }

    @Test
    fun testValidateRatesInvalidDateAbbreviation() {
        val rate = RateDTO("Monday", "0900-2100", "America/Chicago", 950)

        val exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: \"days\" string contains invalid value or is formatted incorrectly")
    }

    @Test
    fun testValidateRatesWhitespaceInsteadofCommasInDays() {
        val rate = RateDTO("mon tues", "0900-2100", "America/Chicago", 950)

        val exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: \"days\" string contains invalid value or is formatted incorrectly")
    }

    @Test
    fun testValidateTimesFormatting() {
        // Times include colons
        var rate = RateDTO("mon", "09:00-21:00", "America/Chicago", 950)

        var exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Times formatted incorrectly")

        // Times contains spaces
        rate = RateDTO("mon", "0900 - 2100", "America/Chicago", 950)

        exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Times formatted incorrectly")

        // Times contains non-numbers
        rate = RateDTO("mon", "nine-2100", "America/Chicago", 950)

        exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Times formatted incorrectly")

        // Times doesn't include leading 0
        rate = RateDTO("mon", "900 - 2100", "America/Chicago", 950)

        exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Times formatted incorrectly")
    }

    @Test
    fun testValidateSameStartAndEndTimes() {
        val rate = RateDTO("mon", "0900-0900", "America/Chicago", 950)

        val exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Rate end time must be after start time")
    }

    @Test
    fun testValidateRatesInvalidTimes() {
        // Start time minutes greater than 59
        var rate = RateDTO("mon", "0960-2100", "America/Chicago", 950)

        var exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Rate time invalid")

        // End time minutes greater than 59
        rate = RateDTO("mon", "0900-2171", "America/Chicago", 950)

        exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Rate time invalid")

        // End time hour greater than 23
        rate = RateDTO("mon", "0900-2400", "America/Chicago", 950)

        exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: Rate time invalid")
    }

    @Test
    fun validateRatesInvalidTimezone() {
        // Non-existing time zone
        var rate = RateDTO("mon", "0900-2100", "America/Austin", 950)

        var exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: America/Austin is not a valid timezone")

        // Common format timezone, i.e. not ISO 8601
        rate = RateDTO("mon", "0900-2100", "CST", 950)

        exception = assertThrows<ValidationException> {
            ratesValidator.validateRates(Rates(listOf(rate)))
        }

        assertThat(exception.message).isEqualTo("Invalid request: CST is not a valid timezone")
    }
}