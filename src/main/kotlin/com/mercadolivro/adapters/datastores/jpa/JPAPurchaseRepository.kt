package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecord
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecord
import com.mercadolivro.adapters.datastores.jpa.records.PurchaseRecord
import com.mercadolivro.adapters.datastores.jpa.records.PurchaseRecordRepository
import com.mercadolivro.core.entities.Purchase
import com.mercadolivro.core.use_cases.ports.PurchaseRepository
import java.time.LocalDateTime

class JPAPurchaseRepository(
    private val purchaseRecordRepository: PurchaseRecordRepository
) : JPAGenericRepository<Purchase, PurchaseRecord>(purchaseRecordRepository, PurchaseRecord),
    PurchaseRepository {
    override fun create(input: PurchaseRepository.CreateInput): Purchase {
        val purchaseRecord = PurchaseRecord(
            books = input.bookIds.toList().map { BookRecord(id = it) },
            price = input.price,
            createdAt = LocalDateTime.now(),
            customer = CustomerRecord(id = input.customerId),
        )
        return purchaseRecordRepository.save(purchaseRecord).toEntity()
    }
}
