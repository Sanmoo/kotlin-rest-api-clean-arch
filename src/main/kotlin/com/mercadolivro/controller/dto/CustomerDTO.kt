package com.mercadolivro.controller.dto

import com.mercadolivro.core.entities.CustomerStatus
import javax.validation.constraints.NotBlank

data class CustomerDTO(
    val id: Int?,

    @field:NotBlank(message = "Must not be empty")
    val name: String,

    @field:NotBlank
    val email: String,

    val status: CustomerStatus?
)
