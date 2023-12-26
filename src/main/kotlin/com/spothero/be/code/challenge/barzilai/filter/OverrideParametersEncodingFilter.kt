package com.spothero.be.code.challenge.barzilai.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

/**
 * Along with OverrideParametersEncodingRequestWrapper, intercepts each request to price endpoint,
 * finds any instance of a space character, and converts it to '+'.
 * This is necessary because the '+' character in URL encoding is used for a space,
 * however, for timestamps, the '+' is used in time zones.
 * This filter allows this API to accept non-encoded timestamps in the query parameters
 */
@Component
class OverrideParametersEncodingFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        chain.doFilter(OverrideParametersEncodingRequestWrapper(req), response)
    }
}