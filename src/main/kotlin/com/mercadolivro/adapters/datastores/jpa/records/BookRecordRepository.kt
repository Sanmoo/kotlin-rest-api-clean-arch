package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.BookStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BookRecordRepository : JpaRepository <BookRecord, Int> {
    fun findAllByName(name: String, pageable: Pageable): P<BookRecord>
    fun findAllByStatus(status: BookStatus, pageable: Pageable): List<BookRecord>
    fun findAllByNameAndStatus(name: String, status: BookStatus, pageable: Pageable): List<BookRecord>
    fun findByCustomer(fromEntity: CustomerRecord, pageable: Pageable): List<BookRecord>
}
