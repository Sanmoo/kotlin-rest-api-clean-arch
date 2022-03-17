package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.GenericRepository

class GetCustomerDetails(private val customerRepository: GenericRepository<Customer>) {
    data class Input(val id: Int)
    data class Output(val id: Int, val email: String, val name: String)

    fun detail(input: Input): Output? {
        val c = customerRepository.getById(input.id)
        return Output(id = c?.id ?: return null, name = c.name, email = c.email)
    }
}
