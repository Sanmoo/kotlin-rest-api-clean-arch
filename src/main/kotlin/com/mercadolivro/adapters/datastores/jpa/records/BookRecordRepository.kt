package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.BookStatus
import org.springframework.data.repository.CrudRepository

interface BookRecordRepository : CrudRepository <BookRecord, Int> {
    fun findAllByName(name: String): List<BookRecord>
    fun findAllByStatus(status: BookStatus): List<BookRecord>
    fun findAllByNameAndStatus(name: String, status: BookStatus): List<BookRecord>
    fun findByCustomer(fromEntity: CustomerRecord): List<BookRecord>
}
