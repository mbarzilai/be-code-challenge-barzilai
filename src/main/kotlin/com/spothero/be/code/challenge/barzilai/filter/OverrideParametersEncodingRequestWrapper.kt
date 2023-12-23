package com.spothero.be.code.challenge.barzilai.filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper


class OverrideParametersEncodingRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val overriddenParameterMap: Map<String, Array<String>> = request.parameterMap
        .mapValues { (key,value) ->
            if (key == "start" || key == "end") {
                value.forEachIndexed { index, valueEntry -> value[index] = valueEntry.replace(" ", "+") }
            }
            value
        }

    override fun getParameterMap(): Map<String, Array<String>> {
        return overriddenParameterMap
    }

    override fun getParameterValues(name: String): Array<String>? {
        return overriddenParameterMap[name]
    }

    override fun getParameter(name: String): String? {
        return getParameterValues(name)?.get(0)
    }
}