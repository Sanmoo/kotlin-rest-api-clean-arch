package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.GenericRepository

class CreateCustomer(private val customerRepository: GenericRepository<Customer>) {
    data class Input(val name: String, val email: String)
    data class Output(val id: Int, val name: String, val email: String)

    fun createCustomer(i: Input): Output {
        val customer = Customer(name = i.name, email = i.email)
        val id = customerRepository.create(customer)
        return Output(id, i.name, i.email)
    }
}
