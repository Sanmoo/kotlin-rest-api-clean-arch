package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.BookStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BookRecordRepository : JpaRepository <BookRecord, Int> {
    fun findAllByName(name: String, pageable: Pageable): Page<BookRecord>
    fun findAllByStatus(status: BookStatus, pageable: Pageable): Page<BookRecord>
    fun findAllByNameAndStatus(name: String, status: BookStatus, pageable: Pageable): Page<BookRecord>
    fun findByCustomer(fromEntity: CustomerRecord, pageable: Pageable): Page<BookRecord>
}
