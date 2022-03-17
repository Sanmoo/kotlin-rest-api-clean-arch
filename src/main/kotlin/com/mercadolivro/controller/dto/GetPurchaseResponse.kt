package com.mercadolivro.controller.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class GetPurchaseResponse(
    val id: Int,
    val customerId: Int,
    val bookIds: Set<Int>,
    val nfe: String?,
    val price: BigDecimal,
    val createdAt: LocalDateTime
)
