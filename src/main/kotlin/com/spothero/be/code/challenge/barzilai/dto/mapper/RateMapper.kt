package com.spothero.be.code.challenge.barzilai.dto.mapper

import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.model.Rate
import org.springframework.stereotype.Component

@Component
class RateMapper {
    fun rateDTOToEntity(rateDTO: RateDTO): Rate {
        val daysList = rateDTO.days.split(",")
            .filter { DayOfWeekMapper.isValidAbbreviation(it) }
            .map {  DayOfWeekMapper.getDayOfWeekByAbbreviation(it) }
        val timesList = rateDTO.times.split("-")
        return Rate(daysList, timesList[0], timesList[1], rateDTO.tz, rateDTO.price)
    }

    fun rateEntityToDTO(rate: Rate): RateDTO {
        val days = rate.days
            .map { DayOfWeekMapper.getAbbreviationByDayOfWeek(it) }
            .joinToString(",")
        val times = "${rate.startTime}-${rate.endTime}"
        return RateDTO(days, times, rate.timeZone, rate.price)
    }
}