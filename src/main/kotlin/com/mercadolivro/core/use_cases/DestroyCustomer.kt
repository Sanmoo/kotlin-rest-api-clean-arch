package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.GenericRepository
import com.mercadolivro.core.use_cases.ports.PaginationData

class DestroyCustomer(
    private val customerRepository: GenericRepository<Customer>,
    private val bookRepository: BookRepository
) {
    data class Input(val id: Int)

    fun destroy(i: Input) {
        val customer = customerRepository.getById(i.id) ?: throw Exception("Customer not found")
        if (customer.status != CustomerStatus.ACTIVE) {
            throw Exception("User is already not active")
        }

        val books = bookRepository
            .findByCustomer(customer, PaginationData(1000, 0))
            .content.map { it.copy(status = BookStatus.DELETED, customer = null) }
        bookRepository.updateAll(books)
        customerRepository.update(customer.id, customer.copy(status = CustomerStatus.INACTIVE))
    }
}
