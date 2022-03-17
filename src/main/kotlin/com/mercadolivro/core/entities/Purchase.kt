package com.mercadolivro.core.entities

import java.math.BigDecimal
import java.time.LocalDateTime

data class Purchase(
    override val id: Int,
    val customerId: Int,
    val bookIds: Set<Int>,
    val nfe: String?,
    val price: BigDecimal,
    val createdAt: LocalDateTime
) : Entity
