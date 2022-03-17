package com.mercadolivro.controller.dto

import com.mercadolivro.core.entities.CustomerStatus

data class GetCustomerResponse(
    val id: Int,
    val name: String,
    val email: String,
    val status: CustomerStatus
)
