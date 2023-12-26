package com.spothero.be.code.challenge.barzilai.controller

import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.dto.validation.RatesValidator
import com.spothero.be.code.challenge.barzilai.service.RateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("rates")
class RatesController(
    private val rateService: RateService,
    private val ratesValidator: RatesValidator
) {

    @Operation(summary = "Get stored rates", description = "Get stored rates")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = Rates::class)) ]
        )
    ])
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRates(): ResponseEntity<Rates> {
        return ResponseEntity(Rates(rateService.getAllRates()), HttpStatus.OK)
    }

    @Operation(
        summary = "Update stored rates",
        description = "Update rate information by submitting a rates JSON. This submitted JSON overwrites the stored rates. Returns the stored list of rates."
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = Rates::class)) ]
        )
    ])
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRates(
        @RequestBody ratesBody: Rates
    ): ResponseEntity<Rates> {
        ratesValidator.validateRates(ratesBody)
        val updatedRates = rateService.updateAllRates(ratesBody.rates)
        return ResponseEntity(Rates(updatedRates), HttpStatus.OK)
    }
}