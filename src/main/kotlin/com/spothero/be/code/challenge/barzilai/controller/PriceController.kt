package com.spothero.be.code.challenge.barzilai.controller

import com.spothero.be.code.challenge.barzilai.dto.model.PriceResponse
import com.spothero.be.code.challenge.barzilai.exception.UnavailableException
import com.spothero.be.code.challenge.barzilai.exception.ValidationException
import com.spothero.be.code.challenge.barzilai.repository.RateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("price")
class PriceController {

    @Autowired
    lateinit var rateRepository: RateRepository

    @GetMapping(produces = ["application/json"])
    fun getPrice(
        @RequestParam("start")
        start: Instant,
        @RequestParam("end")
        end: Instant
    ): ResponseEntity<Any> {
        validateStartAndEnd(start, end)
        val price: Int? = rateRepository.findPriceByStartAndEndInstants(start, end)
        return price?.let { ResponseEntity(PriceResponse(it), HttpStatus.OK) } ?: throw UnavailableException()
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