package com.mercadolivro.controller.dto

data class ErrorResponse(
    val httpCode: Int,
    val message: String,
    val internalCode: String,
    val errors: List<FieldErrorResponse>?,
)
