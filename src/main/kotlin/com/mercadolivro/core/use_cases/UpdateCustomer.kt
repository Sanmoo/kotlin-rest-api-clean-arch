package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.ports.Encryptor
import com.mercadolivro.core.use_cases.ports.GenericRepository

class UpdateCustomer(
    private val customerRepository: GenericRepository<Customer>,
    private val encryptor: Encryptor
) {
    data class Input(val id: Int, val name: String?, val email: String?, val status: CustomerStatus?, val password: String?, val roles: Set<Role>?)

    fun update(c: Input) {
        val customer = customerRepository.getById(c.id) ?: throw Errors.ML201.toResourceNotFoundException(c.id)

        if (customer.status == CustomerStatus.INACTIVE) {
            throw Errors.ML203.toOperationNotAllowed()
        }

        customerRepository.update(c.id, Customer(
            id = c.id,
            name = c.name ?: customer.name,
            email = c.email ?: customer.email,
            status = c.status ?: customer.status,
            password = c.password?.let{ encryptor.encrypt(it) } ?: customer.password,
            roles = c.roles ?: customer.roles
        ))
    }
}
