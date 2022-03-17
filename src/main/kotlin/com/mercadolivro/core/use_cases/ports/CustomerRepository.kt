package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Customer

interface CustomerRepository : GenericRepository<Customer> {
    fun getAllByName(name: String): List<Customer>
}
