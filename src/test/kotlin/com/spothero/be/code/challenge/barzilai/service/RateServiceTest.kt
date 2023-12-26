package com.spothero.be.code.challenge.barzilai.service

import com.spothero.be.code.challenge.barzilai.DatabaseContainerBase
import com.spothero.be.code.challenge.barzilai.dto.model.RateDTO
import com.spothero.be.code.challenge.barzilai.model.Rate
import com.spothero.be.code.challenge.barzilai.repository.RateRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.Instant

@SpringBootTest
@Transactional
class RateServiceTest : DatabaseContainerBase() {

    @Autowired
    private lateinit var rateService: RateService

    @Autowired
    private lateinit var rateRepository: RateRepository

    @BeforeEach
    fun init() {
        rateRepository.deleteAll()

        val rate1 = Rate(
            listOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
            "09",
            "00",
            "21",
            "00",
            "America/Chicago",
            2000
        )
        val rate2 = Rate(
            listOf(DayOfWeek.WEDNESDAY),
            "06",
            "00",
            "18",
            "00",
            "America/Chicago",
            1750
        )
        rateRepository.saveAll(listOf(rate1, rate2))
    }

    @Test
    fun testGetAllRates() {
        val ratesList: List<RateDTO> = rateService.getAllRates()

        assertThat(ratesList).isNotNull()
        assertThat(ratesList).hasSize(2)
    }

    @Test
    fun testGetAllRates_NoRatesInDB() {
        rateRepository.deleteAll()

        val ratesList: List<RateDTO> = rateService.getAllRates()

        assertThat(ratesList).isNotNull()
        assertThat(ratesList).isEmpty()
    }

    @Test
    fun testUpdateAllRates() {
        // confirm that database already has existing rates
        val existingRates = rateService.getAllRates()
        assertThat(existingRates).isNotNull
        assertThat(existingRates).hasSize(2)

        val newRate1 = RateDTO("fri", "0900-2100", "America/New_York", 2000)
        val newRate2 = RateDTO("sat", "0900-2100", "America/New_York", 2500)
        val newRate3 = RateDTO("sun", "0900-2100", "America/New_York", 2250)

        val savedRates = rateService.updateAllRates(listOf(newRate1, newRate2, newRate3))

        assertThat(savedRates).hasSize(3)
        assertThat(savedRates).containsExactly(newRate1, newRate2, newRate3)

        // confirm that old rates were completely overridden and contain only new rates
        val newRates = rateService.getAllRates()

        assertThat(newRates).hasSize(3)
        assertThat(newRates).containsExactly(newRate1, newRate2, newRate3)
    }

    @Test
    fun testUpdateAllRates_EmptyList() {
        // confirm that database already has existing rates
        val existingRates = rateService.getAllRates()
        assertThat(existingRates).isNotNull
        assertThat(existingRates).hasSize(2)

        val savedRates = rateService.updateAllRates(listOf())

        assertThat(savedRates).isNotNull
        assertThat(savedRates).isEmpty()

        // confirm that old rates were completely overridden and contain only new rates
        val newRates = rateService.getAllRates()

        assertThat(newRates).isNotNull
        assertThat(newRates).isEmpty()
    }

    @Test
    fun testGetRatePriceByStartAndEnd() {
        val start = Instant.parse("2015-07-04T15:00:00+00:00")
        val end = Instant.parse("2015-07-04T20:00:00+00:00")

        val price = rateService.getRatePriceByStartAndEnd(start, end)

        assertThat(price).isEqualTo(2000)
    }

    @Test
    fun testGetRatePriceByStartAndEnd_NoneFound() {
        val start = Instant.parse("2015-07-02T15:00:00+00:00")
        val end = Instant.parse("2015-07-02T20:00:00+00:00")

        val price = rateService.getRatePriceByStartAndEnd(start, end)

        assertThat(price).isNull()
    }


}