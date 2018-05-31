package com.github.hughwphamill.springtraceidlogref

import brave.Tracer
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.VndErrors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BrokenController {

    @GetMapping("/broken")
    fun broken(): Unit = throw LocalException("Something when wrong!")
}

class LocalException(message: String) : RuntimeException(message)

@ControllerAdvice
class ExceptionHandler {

    @Autowired
    private lateinit var tracer: Tracer

    @ExceptionHandler(LocalException::class)
    fun handleLocalException(e: LocalException): ResponseEntity<VndErrors.VndError> {
        val traceId = MDC.get("X-B3-TraceId") // <-- Is this the correct way of getting trace id from Sleuth?
        val traceId2 = tracer.currentSpan().context().traceIdString() // <-- Or something like this?
        val error = VndErrors.VndError(traceId, e.message)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}