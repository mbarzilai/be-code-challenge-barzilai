package com.spothero.be.code.challenge.barzilai.dto.model

import io.swagger.v3.oas.annotations.media.Schema

data class Rates(
    @Schema(description = "List of valid rates. Must not overlap.")
    val rates: List<RateDTO>
)
