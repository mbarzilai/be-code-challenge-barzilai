package com.spothero.be.code.challenge.barzilai.dto.model

import io.swagger.v3.oas.annotations.media.Schema

data class RateDTO(
    @Schema(
        description = "Comma separated string of days of the week the rate applies to. " +
                "Accepted abbreviations for each day of the week are: mon, tues, wed, thurs, fri, sat, sun",
        example = "mon,tues,thurs"
    )
    val days: String,
    @Schema(
        description = "Time range the rate is valid. Start time and end time are represented as four numbers (no colon) and are separated by a dash(-). " +
                "Use 0000 to represent midnight. End time must not be greater than 2359",
        example = "0900-2100"
    )
    val times: String,
    @Schema(
        description = "Time zone for rate times. Must adhere to 2017c version of the tz database",
        example = "America/Chicago"
    )
    val tz: String,
    @Schema(
        description = "Price of the rate",
        example = "1500"
    )
    val price: Int,
)
