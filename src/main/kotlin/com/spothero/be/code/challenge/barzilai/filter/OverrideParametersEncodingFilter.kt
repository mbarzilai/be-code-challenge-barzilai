package com.spothero.be.code.challenge.barzilai.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component


@Component
class OverrideParametersEncodingFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        chain.doFilter(OverrideParametersEncodingRequestWrapper(req), response)
    }
}