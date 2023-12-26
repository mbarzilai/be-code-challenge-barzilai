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
        return Rate(
            daysList,
            timesList[0].substring(0, 2),
            timesList[0].substring(2),
            timesList[1].substring(0, 2),
            timesList[1].substring(2),
            rateDTO.tz,
            rateDTO.price
        )
    }

    fun rateEntityToDTO(rate: Rate): RateDTO {
        val days = rate.days.joinToString(",") { DayOfWeekMapper.getAbbreviationByDayOfWeek(it) }
        val times = "${rate.startHour}${rate.startMin}-${rate.endHour}${rate.endMin}"
        return RateDTO(days, times, rate.timeZone, rate.price)
    }
}