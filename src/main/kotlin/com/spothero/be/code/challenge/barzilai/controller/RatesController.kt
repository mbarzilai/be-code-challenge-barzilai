package com.spothero.be.code.challenge.barzilai.controller

import com.spothero.be.code.challenge.barzilai.dto.model.Rates
import com.spothero.be.code.challenge.barzilai.service.RateService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("rates")
class RatesController(private val rateService: RateService) {

    @GetMapping(produces = ["application/json"])
    fun getRates(): ResponseEntity<Rates> {
        return ResponseEntity(Rates(rateService.getAllRates()), HttpStatus.OK)
    }

    @PutMapping(produces = ["application/json"])
    fun updateRates(
        @RequestBody ratesBody: Rates
    ): ResponseEntity<Rates> {
        val updatedRates = rateService.updateAllRates(ratesBody.rates)
        return ResponseEntity(Rates(rateService.getAllRates()), HttpStatus.OK)
    }
}