package com.mercadolivro.controller.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest (
    @field:NotBlank(message = "Must not be empty")
    val name: String,

    @field:Email
    val email: String,

    @field:NotEmpty(message = "Password must be provided")
    val password: String,
)
