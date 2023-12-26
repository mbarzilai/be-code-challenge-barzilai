package com.spothero.be.code.challenge.barzilai.model

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.DayOfWeek

@Entity
@Table
class Rate(

    @ElementCollection(targetClass = DayOfWeek::class)
    @Column(name = "dayOfWeek", nullable = false)
    val days: List<DayOfWeek>,

    @Column(nullable = false)
    val startHour: String,

    @Column(nullable = false)
    val startMin: String,

    @Column(nullable = false)
    val endHour: String,

    @Column(nullable = false)
    val endMin: String,

    @Column(nullable = false)
    val timeZone: String,

    @Column(nullable = false)
    val price: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
)