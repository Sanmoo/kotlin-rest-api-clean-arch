package com.mercadolivro.controller.dto

import com.mercadolivro.core.entities.CustomerStatus
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UpdateCustomerRequest(
    val id: Int?,

    @field:NotBlank(message = "Must not be empty")
    val name: String,

    @field:Email
    val email: String,

    @field:NotBlank
    val status: CustomerStatus?,

    val password: String?
)
