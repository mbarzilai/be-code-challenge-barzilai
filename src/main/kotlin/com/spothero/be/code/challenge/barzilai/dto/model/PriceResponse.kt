package com.spothero.be.code.challenge.barzilai.dto.model

import io.swagger.v3.oas.annotations.media.Schema

data class PriceResponse(
    @Schema(example = "5000")
    val price: Int
)
