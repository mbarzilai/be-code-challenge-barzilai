package com.spothero.be.code.challenge.barzilai.exception

class ValidationException(message: String?) : RuntimeException("Invalid request: $message")