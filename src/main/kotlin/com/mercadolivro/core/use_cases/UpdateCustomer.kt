package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.use_cases.ports.GenericRepository

class UpdateCustomer(private val customerRepository: GenericRepository<Customer>) {
    data class Input(val id: Int, val name: String?, val email: String?, val status: CustomerStatus?)

    fun update(c: Input) {
        val customer = customerRepository.getById(c.id) ?: throw Exception("Customer with id ${c.id} does not exist")

        if (customer.status == CustomerStatus.INACTIVE) {
            throw Exception("It is not possible to update an INACTIVE customer")
        }

        customerRepository.update(c.id, Customer(
            id = c.id,
            name = c.name ?: customer.name,
            email = c.email ?: customer.email,
            status = c.status ?: customer.status
        ))
    }
}
