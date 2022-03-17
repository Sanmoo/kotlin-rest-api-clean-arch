package com.mercadolivro.adapters.datastores.in_memory

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.CreateCustomer
import com.mercadolivro.core.use_cases.ports.CustomerRepository

class CustomerInMemoryRepository : GenericInMemoryRepository<Customer>(), CustomerRepository {
    override fun create(input: CreateCustomer.Input): Customer {
        val newIndex = index++
        val newRecord = Customer(id = newIndex, name = input.name, email = input.email)
        store[newIndex] = newRecord
        return newRecord
    }

    override fun getAllByName(name: String): List<Customer> {
        return store.values.filter { it.name == name }
    }
}
