package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.ports.CustomerRepository

class CreateCustomer(private val customerRepository: CustomerRepository) {
    data class Input(val name: String, val email: String)
    data class Output(val id: Int, val name: String, val email: String, val status: CustomerStatus)

    fun createCustomer(i: Input): Output {
        if (customerRepository.existsByEmail(i.email)) {
            throw Errors.ML204.toOperationNotAllowed(i.email)
        }

        val customer = customerRepository.create(name = i.name, email = i.email, status = CustomerStatus.ACTIVE)
        return Output(customer.id, customer.name, customer.email, customer.status)
    }
}
