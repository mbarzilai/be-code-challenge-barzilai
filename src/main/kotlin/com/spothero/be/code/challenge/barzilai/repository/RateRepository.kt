package com.spothero.be.code.challenge.barzilai.repository

import com.spothero.be.code.challenge.barzilai.model.Rate
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface RateRepository : CrudRepository<Rate, Int> {

    @Query(value = QUERY_STRING, nativeQuery = true)
    fun findPriceByStartAndEndInstants(
        @Param("requestStart") requestStart: Instant,
        @Param("requestEnd") requestEnd: Instant
    ) : Int?

    companion object {
        private const val QUERY_STRING = """
SELECT
    r.price
FROM
    rate r
    JOIN rate_days rd ON r.id = rd.rate_id
WHERE
    rd.day_of_week = extract(isodow FROM :requestStart at time zone r.time_zone)
    AND rd.day_of_week = extract(isodow FROM :requestEnd at time zone r.time_zone)
    AND make_time(cast(r.start_hour as int), cast(r.start_min as int), 00) <= cast(:requestStart at time zone r.time_zone as time)
    AND make_time(cast(r.end_hour as int), cast(r.end_min as int), 00) >= cast(:requestEnd at time zone r.time_zone as time)
"""
    }
}