package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.GenericRepository

class UpdateCustomer(private val customerRepository: GenericRepository<Customer>) {
    data class Input(val id: Int, val name: String, val email: String)

    fun update(c: Input) {
        customerRepository.update(c.id, Customer(id = c.id, name = c.name, email = c.email))
    }
}
