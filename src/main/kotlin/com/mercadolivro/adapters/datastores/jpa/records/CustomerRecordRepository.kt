package com.mercadolivro.adapters.datastores.jpa.records

import org.springframework.data.repository.CrudRepository

interface CustomerRecordRepository : CrudRepository <CustomerRecord, Int> {
    fun findAllByName(name: String): List<CustomerRecord>
}
