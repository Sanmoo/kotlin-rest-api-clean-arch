package com.mercadolivro.core.use_cases

import com.mercadolivro.core.use_cases.ports.CustomerRepository

class CreateCustomer(private val customerRepository: CustomerRepository) {
    data class Input(val name: String, val email: String)
    data class Output(val id: Int, val name: String, val email: String)

    fun createCustomer(i: Input): Output {
        val customer = customerRepository.create(i)
        return Output(customer.id, customer.name, customer.email)
    }
}
