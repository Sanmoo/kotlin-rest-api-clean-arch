package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.CreateCustomer

interface CustomerRepository : GenericRepository<Customer> {
    fun create(input: CreateCustomer.Input): Customer
    fun getAllByName(name: String, paginationData: PaginationData): List<Customer>
}
