package com.mercadolivro.controller.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.core.entities.BookStatus
import java.math.BigDecimal

data class PartialBookDTO(
    val name: String?,
    val price: BigDecimal?,
    val status: BookStatus?,

    @JsonAlias("customer_id")
    val customerId: Int?,
)
