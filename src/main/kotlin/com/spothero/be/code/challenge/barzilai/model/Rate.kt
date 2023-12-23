package com.spothero.be.code.challenge.barzilai.model

import jakarta.persistence.*
import java.time.DayOfWeek

@Entity
@Table
class Rate(

//    @Enumerated(EnumType.ORDINAL)
    @ElementCollection(targetClass = DayOfWeek::class)
//    @CollectionTable(name = "DayOfWeek", joinColumns = @JoinColumn(name = "rateId"))
    @Column(name = "dayOfWeek", nullable = false)
    val days: Set<DayOfWeek>,

    @Column(nullable = false)
    val startTime: String,

    @Column(nullable = false)
    val endTime: String,

    @Column(nullable = false)
    val timeZone: String,

    @Column(nullable = false)
    val price: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
)