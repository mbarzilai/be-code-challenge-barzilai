package com.spothero.be.code.challenge.barzilai.dto.mapper

import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.model.Rate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.DayOfWeek

class RateMapperTest {

    private val rateMapper = RateMapper()

    @Test
    fun testRateDTOToEntity() {
        val rateDTO = RateDTO("mon,tues,wed,thurs,fri,sat,sun", "0930-1200", "America/Denver", 1200)

        val rateEntity = rateMapper.rateDTOToEntity(rateDTO)

        assertThat(rateEntity.days).containsExactly(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        assertThat(rateEntity.startHour).isEqualTo("09")
        assertThat(rateEntity.startMin).isEqualTo("30")
        assertThat(rateEntity.endHour).isEqualTo("12")
        assertThat(rateEntity.endMin).isEqualTo("00")
        assertThat(rateEntity.timeZone).isEqualTo("America/Denver")
        assertThat(rateEntity.price).isEqualTo(1200)
    }

    @Test
    fun testRateEntityToDTO() {
        val rateEntity = Rate(
            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
            "01",
            "00",
            "08",
            "30",
            "UTC",
            1750
        )

        val rateDTO = rateMapper.rateEntityToDTO(rateEntity)

        assertThat(rateDTO.days).isEqualTo("mon,tues,wed,thurs,fri,sat,sun")
        assertThat(rateDTO.times).isEqualTo("0100-0830")
        assertThat(rateDTO.tz).isEqualTo("UTC")
        assertThat(rateDTO.price).isEqualTo(1750)
    }

}