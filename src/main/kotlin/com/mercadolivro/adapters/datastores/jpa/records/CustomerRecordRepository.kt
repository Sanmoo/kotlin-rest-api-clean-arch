package com.mercadolivro.adapters.datastores.jpa.records

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRecordRepository : JpaRepository <CustomerRecord, Int> {
    fun findAllByName(name: String, pageable: Pageable): Page<CustomerRecord>
    fun existsByEmail(email: String): Boolean
}
