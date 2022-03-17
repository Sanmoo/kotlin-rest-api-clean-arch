package com.mercadolivro.controller

import com.mercadolivro.controller.dto.ErrorResponse
import com.mercadolivro.core.use_cases.exceptions.OperationNotAllowed
import com.mercadolivro.core.use_cases.exceptions.ResourceNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(ResourceNotFound::class)
    fun handleException(ex: ResourceNotFound, request: WebRequest): ResponseEntity<ErrorResponse> {
        val err = ErrorResponse(
            httpCode = HttpStatus.NOT_FOUND.value(),
            message = ex.message,
            internalCode = ex.code,
            errors = null
        )

        return ResponseEntity(err, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(OperationNotAllowed::class)
    fun handleException(ex: OperationNotAllowed, request: WebRequest): ResponseEntity<ErrorResponse> {
        val err = ErrorResponse(
            httpCode = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            message = "Reason: ${ex.reason}",
            internalCode = ex.code,
            errors = null
        )

        return ResponseEntity(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
