package com.spothero.be.code.challenge.barzilai.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException::class)
    fun handleResponseValidationException(e: ValidationException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UnavailableException::class)
    fun handleResponseUnavailableException(e: UnavailableException): ResponseEntity<String> {
        return ResponseEntity("unavailable", HttpStatus.NOT_FOUND)
    }
}