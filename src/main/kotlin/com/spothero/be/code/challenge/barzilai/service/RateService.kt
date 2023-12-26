package com.spothero.be.code.challenge.barzilai.service

import com.spothero.be.code.challenge.barzilai.dto.mapper.RateMapper
import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.repository.RateRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RateService(
    private val rateRepository: RateRepository,
    private val rateMapper: RateMapper
) {

    val log: Logger = LoggerFactory.getLogger(RateService::class.java)
    fun getAllRates(): List<RateDTO> {
        try {
            val ratesEntities = rateRepository.findAll()
            return ratesEntities.map { rateMapper.rateEntityToDTO(it) }
        } catch (e: Exception) {
            log.error("Error getting rates", e)
            throw e
        }
    }

    fun updateAllRates(rates: List<RateDTO>): List<RateDTO> {
        try {
            val ratesEntitiesToSave = rates.map { rateMapper.rateDTOToEntity(it) }
            // Clear all currently existing rates in the database since update is intended to be a full override
            rateRepository.deleteAll()
            val savedRates = rateRepository.saveAll(ratesEntitiesToSave)
            return savedRates.map { rateMapper.rateEntityToDTO(it) }
        } catch (e: Exception) {
            log.error("Error updating rates", e)
            throw e
        }
    }

    fun getRatePriceByStartAndEnd(start: Instant, end: Instant): Int? {
        try {
            return rateRepository.findPriceByStartAndEndInstants(start, end)
        } catch (e: Exception) {
            log.error("Error getting price", e)
            throw e
        }
    }
}