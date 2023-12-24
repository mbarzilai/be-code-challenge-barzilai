package com.spothero.be.code.challenge.barzilai.dto.validation

import com.spothero.be.code.challenge.barzilai.dto.mapper.DayOfWeekMapper
import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import org.springframework.stereotype.Component
import java.time.zone.ZoneRulesException
import java.time.zone.ZoneRulesProvider

@Component
class RatesValidator {
    fun validateRates(ratesList: Rates) {
        ratesList.rates.forEach { validateRate(it) }
    }

    private fun validateRate(rate: RateDTO) {
        validateDays(rate.days)
        validateTimes(rate.times)
        validateTimeZone(rate.tz)
    }

    private fun validateDays(days: String) {
        days.split(",").forEach {
            if (!DayOfWeekMapper.isValidAbbreviation(it)) {
                throw ValidationException("\"days\" string contains invalid value or is formatted incorrectly")
            }
        }
    }

    private fun validateTimes(times: String) {
        if (!Regex("\\d{4}-\\d{4}").matches(times)) {
            throw ValidationException("Times formatted incorrectly")
        }
        val timeParts = times.split("-")
        if (timeParts[0] >= timeParts[1]) {
            throw ValidationException("Rate end time must be after start time")
        }
        if (timeParts[0].substring(2).toInt() > 59
            || timeParts[1].substring(2).toInt() > 59
            || timeParts[1].substring(0, 2).toInt() > 23) {
            throw ValidationException("Rate time invalid")
        }
    }

    private fun validateTimeZone(timeZone: String) {
        try {
            ZoneRulesProvider.getRules(timeZone, false)
        } catch (e: ZoneRulesException) {
            throw ValidationException("$timeZone is not a valid timezone")
        }
    }
}