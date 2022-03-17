package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.Encryptor

class CreateCustomer(
    private val customerRepository: CustomerRepository,
    private val passwordEncrypter: Encryptor
) {
    data class Input(val name: String, val email: String, val password: String)
    data class Output(val id: Int, val name: String, val email: String, val status: CustomerStatus)

    fun createCustomer(i: Input): Output {
        if (customerRepository.existsByEmail(i.email)) {
            throw Errors.ML204.toOperationNotAllowed(i.email)
        }

        val customer = customerRepository.create(
            name = i.name,
            email = i.email,
            status = CustomerStatus.ACTIVE,
            password = passwordEncrypter.encrypt(i.password),
            roles = setOf(Role.CUSTOMER)
        )
        return Output(customer.id, customer.name, customer.email, customer.status)
    }
}
