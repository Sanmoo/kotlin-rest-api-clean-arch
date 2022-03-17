package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Purchase
import java.math.BigDecimal

interface PurchaseRepository : GenericRepository<Purchase> {
    data class CreateInput(val customerId: Int, val bookIds: Set<Int>, val price: BigDecimal)
    fun create(input: CreateInput): Purchase
}
