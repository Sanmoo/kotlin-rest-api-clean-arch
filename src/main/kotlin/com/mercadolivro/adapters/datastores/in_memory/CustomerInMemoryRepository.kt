package com.mercadolivro.adapters.datastores.in_memory

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.use_cases.CreateCustomer
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

class CustomerInMemoryRepository : GenericInMemoryRepository<Customer>(), CustomerRepository {
    override fun create(input: CreateCustomer.Input): Customer {
        val newIndex = index++
        val newRecord = Customer(id = newIndex, name = input.name, email = input.email, status = CustomerStatus.ACTIVE)
        store[newIndex] = newRecord
        return newRecord
    }

    override fun getAllByName(name: String, paginationData: PaginationData): PaginatedResult<Customer> {
        TODO("Not yet implemented")
    }
}
