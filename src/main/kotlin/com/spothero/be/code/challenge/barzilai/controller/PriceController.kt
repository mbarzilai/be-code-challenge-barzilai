package com.spothero.be.code.challenge.barzilai.controller

import com.spothero.be.code.challenge.barzilai.dto.model.PriceResponse
import com.spothero.be.code.challenge.barzilai.exception.UnavailableException
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import com.spothero.be.code.challenge.barzilai.service.RateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("price")
class PriceController(private val rateService: RateService) {

    @Operation(
        summary = "Request the price for a specified time",
        description = "Request the price for a specified start time and end time. If no applicable rate is found or input spans more than one day, returns \"unavailable\""
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = PriceResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Price unavailable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(type = "string", example = "unavailable"))]
        )
    ])
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPrice(
        @Parameter(description = "Start time of request. Specify date/time as ISO-8601 with timezone. Must be before end time.", example = "2015-07-04T15:00:00+00:00")
        @RequestParam("start") start: Instant,
        @Parameter(description = "End time of request. Specify date/time as ISO-8601 with timezone. Must be after start time.", example = "2015-07-04T20:00:00+00:00")
        @RequestParam("end") end: Instant
    ): ResponseEntity<PriceResponse> {
        validateStartAndEnd(start, end)
        val price: Int? = rateService.getRatePriceByStartAndEnd(start, end)
        return price?.let { ResponseEntity(PriceResponse(it), HttpStatus.OK) } ?: throw UnavailableException()
    }

    @ExceptionHandler(UnavailableException::class)
    fun handleResponseUnavailableException(e: UnavailableException): ResponseEntity<String> {
        return ResponseEntity("unavailable", HttpStatus.NOT_FOUND)
    }

    private fun validateStartAndEnd(start: Instant, end: Instant) {
        if (start >= end) {
            throw ValidationException("End time must be after start time")
        }
        if (end.epochSecond - start.epochSecond >= ONE_DAY_IN_SECONDS) {
            throw UnavailableException()
        }
    }

    companion object {
        const val ONE_DAY_IN_SECONDS = 60 * 60 * 24
    }

}