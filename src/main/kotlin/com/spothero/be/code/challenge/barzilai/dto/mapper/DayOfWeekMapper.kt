package com.spothero.be.code.challenge.barzilai.dto.mapper

import java.time.DayOfWeek

enum class DayOfWeekMapper(private val abbreviation: String, private val dayOfWeek: DayOfWeek) {
    MONDAY("mon", DayOfWeek.MONDAY),
    TUESDAY("tues", DayOfWeek.TUESDAY),
    WEDNESDAY("wed", DayOfWeek.WEDNESDAY),
    THURSDAY("thurs", DayOfWeek.THURSDAY),
    FRIDAY("fri", DayOfWeek.FRIDAY),
    SATURDAY("sat", DayOfWeek.SATURDAY),
    SUNDAY("sun", DayOfWeek.SUNDAY);

    companion object {
        fun isValidAbbreviation(abbreviation: String): Boolean {
            return DayOfWeekMapper.entries.map { it.abbreviation }.contains(abbreviation)
        }

        fun getDayOfWeekByAbbreviation(abbreviation: String): DayOfWeek {
            return DayOfWeekMapper.entries.first { it.abbreviation == abbreviation }.dayOfWeek
        }

        fun getAbbreviationByDayOfWeek(dayOfWeek: DayOfWeek): String {
            return DayOfWeekMapper.entries.first { it.dayOfWeek == dayOfWeek }.abbreviation
        }
    }
}