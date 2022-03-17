package com.mercadolivro.core.entities

import java.math.BigDecimal

data class Book(
    override val id: Int,
    val name: String,
    val price: BigDecimal,
    val customer: Customer?,
    val status: BookStatus
) : Entity
