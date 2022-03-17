package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role

interface CustomerRepository : GenericRepository<Customer> {
    fun create(name: String, status: CustomerStatus, email: String, password: String, roles: Set<Role>): Customer
    fun getAllByName(name: String, paginationData: PaginationData): PaginatedResult<Customer>
    fun existsByEmail(email: String): Boolean
    fun getByEmail(email: String): Customer?
}
