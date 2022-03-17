package com.mercadolivro.controller.dto

import com.mercadolivro.core.entities.CustomerStatus

data class UpdateCustomerRequest(
    val name: String? = null,
    val email: String? = null,
    val status: CustomerStatus? = null,
    val password: String? = null
)
