package com.spothero.be.code.challenge.barzilai.repository


import com.spothero.be.code.challenge.barzilai.DatabaseContainerBase
import com.spothero.be.code.challenge.barzilai.model.Rate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.Instant

@SpringBootTest
@Transactional
class RateRepositoryTest : DatabaseContainerBase() {

    @Autowired
    private lateinit var rateRepository: RateRepository

    @Test
    fun testGetAllRates() {
        seedDatabase()
        val rates = rateRepository.findAll()

        assertThat(rates).isNotNull
        assertThat(rates).hasSize(2)

    }

    @Test
    fun testSaveRates() {
        val rate = Rate(
            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
            "09",
            "00",
            "21",
            "00",
            "America/Chicago",
            1500
        )

        val insertedRate = rateRepository.saveAll(listOf(rate))

        val savedRate: Rate? = insertedRate.first().id?.let { rateRepository.findById(it) }?.get()

        assertThat(savedRate).isNotNull
        assertThat(savedRate?.days).containsExactly(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        assertThat(savedRate?.startHour).isEqualTo("09")
        assertThat(savedRate?.startMin).isEqualTo("00")
        assertThat(savedRate?.endHour).isEqualTo("21")
        assertThat(savedRate?.endMin).isEqualTo("00")
        assertThat(savedRate?.timeZone).isEqualTo("America/Chicago")
        assertThat(savedRate?.price).isEqualTo(1500)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_SameTimeZone() {
        seedDatabase()
        val start = Instant.parse("2015-07-01T07:00:00-05:00")
        val end = Instant.parse("2015-07-01T12:00:00-05:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isEqualTo(1750)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_DifferentTimeZone_Success() {
        seedDatabase()
        val start = Instant.parse("2015-07-04T15:00:00+00:00")
        val end = Instant.parse("2015-07-04T20:00:00+00:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isEqualTo(2000)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_DifferentTimeZone_Failure() {
        seedDatabase()
        val start = Instant.parse("2015-07-04T09:00:00+00:00")
        val end = Instant.parse("2015-07-04T20:00:00+00:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isNull()
    }

    @Test
    fun testFindPriceByStartAndEndInstants_NoneMatch() {
        seedDatabase()
        val start = Instant.parse("2015-07-04T07:00:00+05:00")
        val end = Instant.parse("2015-07-04T20:00:00+05:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isNull()
    }

    @Test
    fun testFindPriceByStartAndEndInstants_DifferentTimeZoneDifferentDate_Matches_StandardTime() {
        seedDatabase()
        val start = Instant.parse("2015-07-04T14:00:00+00:00")
        val end = Instant.parse("2015-07-05T02:00:00+00:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isEqualTo(2000)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_DifferentTimeZoneDifferentDate_Matches_DaylightSavingsTime() {
        seedDatabase()
        val start = Instant.parse("2015-12-04T15:00:00+00:00")
        val end = Instant.parse("2015-12-05T03:00:00+00:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isEqualTo(2000)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_DifferentTimeZonesInDatabase() {
        rateRepository.deleteAll()

        val rateLosAngeles = Rate(listOf(DayOfWeek.MONDAY), "09", "00", "09", "59", "America/Los_Angeles", 1000)
        val rateDenver = Rate(listOf(DayOfWeek.MONDAY), "09", "00", "09", "59", "America/Denver", 1500)
        val rateChicago = Rate(listOf(DayOfWeek.MONDAY), "09", "00", "09", "59", "America/Chicago", 2000)
        val rateNewYork = Rate(listOf(DayOfWeek.MONDAY), "09", "00", "09", "59", "America/New_York", 2500)

        rateRepository.saveAll(listOf(rateLosAngeles, rateDenver, rateChicago, rateNewYork))

        // 9 AM - 9:30 AM Denver time
        val start = Instant.parse("2015-12-07T16:00:00+00:00")
        val end = Instant.parse("2015-12-07T16:30:00+00:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isEqualTo(1500)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_MidnightStartTime() {
        rateRepository.deleteAll()

        val rate = Rate(listOf(DayOfWeek.MONDAY), "00", "00", "01", "00", "America/Chicago", 1000)

        rateRepository.saveAll(listOf(rate))

        // 9 AM - 9:30 AM Denver time
        val start = Instant.parse("2015-12-07T00:00:00-06:00")
        val end = Instant.parse("2015-12-07T00:30:00-06:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isEqualTo(1000)
    }

    @Test
    fun testFindPriceByStartAndEndInstants_NonHourlyStartTimes_DoesNotMatchWithStartTimeOnHour() {
        rateRepository.deleteAll()

        val rate = Rate(listOf(DayOfWeek.MONDAY), "09", "11", "10", "00", "America/Chicago", 1000)

        rateRepository.saveAll(listOf(rate))

        // 9 AM - 9:30 AM Denver time
        val start = Instant.parse("2015-12-07T00:09:00-06:00")
        val end = Instant.parse("2015-12-07T00:10:00-06:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isNull()
    }

    @Test
    fun testFindPriceByStartAndEndInstants_NonHourlyStartTimes_MatchesWithStartTimeNotOnHour() {
        rateRepository.deleteAll()

        val rate = Rate(listOf(DayOfWeek.MONDAY), "09", "11", "10", "00", "America/Chicago", 1000)

        rateRepository.saveAll(listOf(rate))

        // 9 AM - 9:30 AM Denver time
        val start = Instant.parse("2015-12-07T00:09:12-06:00")
        val end = Instant.parse("2015-12-07T00:10:00-06:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isNull()
    }

    @Test
    fun testFindPriceByStartAndEndInstants_NonHourlyEndTimes_DoesNotMatchWithEndTimeNotOnHour() {
        rateRepository.deleteAll()

        val rate = Rate(listOf(DayOfWeek.MONDAY), "09", "00", "10", "47", "America/Chicago", 1000)

        rateRepository.saveAll(listOf(rate))

        // 9 AM - 9:30 AM Denver time
        val start = Instant.parse("2015-12-07T00:09:00-06:00")
        val end = Instant.parse("2015-12-07T00:10:52-06:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isNull()
    }

    @Test
    fun testFindPriceByStartAndEndInstants_NonHourlyEndTimes_MatchesWithEndTimeOnHour() {
        rateRepository.deleteAll()

        val rate = Rate(listOf(DayOfWeek.MONDAY), "09", "00", "10", "47", "America/Chicago", 1000)

        rateRepository.saveAll(listOf(rate))

        // 9 AM - 9:30 AM Denver time
        val start = Instant.parse("2015-12-07T00:09:00-06:00")
        val end = Instant.parse("2015-12-07T00:10:00-06:00")

        val price = rateRepository.findPriceByStartAndEndInstants(start, end)

        assertThat(price).isNull()
    }

    private fun seedDatabase() {
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
}