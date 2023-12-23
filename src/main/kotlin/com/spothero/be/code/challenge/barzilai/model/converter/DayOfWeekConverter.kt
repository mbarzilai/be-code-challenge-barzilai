package com.spothero.be.code.challenge.barzilai.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.DayOfWeek

@Converter(autoApply = true)
class DayOfWeekConverter : AttributeConverter<DayOfWeek, Int> {
    override fun convertToDatabaseColumn(dayOfWeek: DayOfWeek): Int {
        return dayOfWeek.value
    }

    override fun convertToEntityAttribute(isoDayOfWeek: Int): DayOfWeek {
        return DayOfWeek.of(isoDayOfWeek)
    }
}