package com.spothero.be.code.challenge.barzilai.repository

import com.spothero.be.code.challenge.barzilai.model.Rate
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface RateRepository : CrudRepository<Rate, Int> {

    @Query(value = """
            SELECT
                r.price
            FROM
                rate r
                JOIN rate_days rd ON r.id = rd.rate_id
            WHERE
                rd.day_of_week = extract(isodow FROM :requestStart at time zone r.time_zone)
                AND rd.day_of_week = extract(isodow FROM :requestEnd at time zone r.time_zone)
                AND r.start_time\:\:time at time zone r.time_zone <= :requestStart\:\:time at time zone r.time_zone
                AND r.end_time\:\:time at time zone r.time_zone >= :requestEnd\:\:time at time zone r.time_zone
            """,
        nativeQuery = true)
    fun findPriceByStartAndEndInstants(
        @Param("requestStart") requestStart: Instant,
        @Param("requestEnd") requestEnd: Instant
    ) : Int?
}