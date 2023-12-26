package com.spothero.be.code.challenge.barzilai.dto.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek

class DayOfWeekMapperTest {

    @Test
    fun testIsValidAbbreviation() {
        assertThat(DayOfWeekMapper.isValidAbbreviation("mon")).isTrue()
        assertThat(DayOfWeekMapper.isValidAbbreviation("tues")).isTrue()
        assertThat(DayOfWeekMapper.isValidAbbreviation("wed")).isTrue()
        assertThat(DayOfWeekMapper.isValidAbbreviation("thurs")).isTrue()
        assertThat(DayOfWeekMapper.isValidAbbreviation("fri")).isTrue()
        assertThat(DayOfWeekMapper.isValidAbbreviation("sat")).isTrue()
        assertThat(DayOfWeekMapper.isValidAbbreviation("sun")).isTrue()


        assertThat(DayOfWeekMapper.isValidAbbreviation("mon ")).isFalse()
        assertThat(DayOfWeekMapper.isValidAbbreviation("Mon")).isFalse()
        assertThat(DayOfWeekMapper.isValidAbbreviation("monday")).isFalse()
    }

    @Test
    fun testGetDayOfWeekByAbbreviation() {
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("mon")).isEqualTo(DayOfWeek.MONDAY)
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("tues")).isEqualTo(DayOfWeek.TUESDAY)
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("wed")).isEqualTo(DayOfWeek.WEDNESDAY)
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("thurs")).isEqualTo(DayOfWeek.THURSDAY)
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("fri")).isEqualTo(DayOfWeek.FRIDAY)
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("sat")).isEqualTo(DayOfWeek.SATURDAY)
        assertThat(DayOfWeekMapper.getDayOfWeekByAbbreviation("sun")).isEqualTo(DayOfWeek.SUNDAY)

        assertThrows<NoSuchElementException> { DayOfWeekMapper.getDayOfWeekByAbbreviation("Monday") }
    }

    @Test
    fun testGetAbbreviationByDayOfWeek() {
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.MONDAY)).isEqualTo("mon")
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.TUESDAY)).isEqualTo("tues")
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.WEDNESDAY)).isEqualTo("wed")
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.THURSDAY)).isEqualTo("thurs")
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.FRIDAY)).isEqualTo("fri")
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.SATURDAY)).isEqualTo("sat")
        assertThat(DayOfWeekMapper.getAbbreviationByDayOfWeek(DayOfWeek.SUNDAY)).isEqualTo("sun")
    }
}