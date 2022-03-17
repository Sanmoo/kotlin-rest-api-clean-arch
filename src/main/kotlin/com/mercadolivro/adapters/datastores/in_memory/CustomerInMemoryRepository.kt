package com.mercadolivro.adapters.datastores.in_memory

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

class CustomerInMemoryRepository : GenericInMemoryRepository<Customer>(), CustomerRepository {
    override fun create(
        name: String,
        status: CustomerStatus,
        email: String,
        password: String,
        roles: Set<Role>
    ): Customer {
        val newIndex = index++
        val newRecord =
            Customer(id = newIndex, name = name, email = email, status = status, password = password, roles = roles)
        store[newIndex] = newRecord
        return newRecord
    }

    override fun getAllByName(name: String, paginationData: PaginationData): PaginatedResult<Customer> {
        TODO("Not yet implemented")
    }

    override fun existsByEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getByEmail(email: String): Customer? {
        TODO("Not yet implemented")
    }
}
