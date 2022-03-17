package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.GenericRepository

class DestroyCustomer(
    private val customerRepository: GenericRepository<Customer>,
    private val bookRepository: BookRepository
) {
    data class Input(val id: Int)

    fun destroy(i: Input) {
        val customer = customerRepository.getById(i.id) ?: throw Exception("Customer not found")
        val books = bookRepository
            .findByCustomer(customer)
            .map { it.copy(status = BookStatus.DELETED, customer = null) }
        bookRepository.updateAll(books)
        customerRepository.deleteById(customer.id)
    }
}
