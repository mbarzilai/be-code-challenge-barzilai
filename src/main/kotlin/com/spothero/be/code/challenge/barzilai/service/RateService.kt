package com.spothero.be.code.challenge.barzilai.service

import com.spothero.be.code.challenge.barzilai.dto.mapper.RateMapper
import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.repository.RateRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RateService(
    private val rateRepository: RateRepository,
    private val rateMapper: RateMapper
) {
    fun getAllRates(): List<RateDTO> {
        val ratesEntities = rateRepository.findAll()
        return ratesEntities.map { rateMapper.rateEntityToDTO(it) }
    }

    fun updateAllRates(rates: List<RateDTO>): List<RateDTO> {
        val ratesEntitiesToSave = rates.map { rateMapper.rateDTOToEntity(it) }
        // Clear all currently existing rates in the database since update is intended to be a full override
        rateRepository.deleteAll()
        val savedRates = rateRepository.saveAll(ratesEntitiesToSave)
        return savedRates.map { rateMapper.rateEntityToDTO(it) }
    }

    fun getRatePriceByStartAndEnd(start: Instant, end: Instant): Int? {
        return rateRepository.findPriceByStartAndEndInstants(start, end)
    }
}