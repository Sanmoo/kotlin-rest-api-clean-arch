package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.GenericRepository
import java.math.BigDecimal

class UpdateBook(
    private val bookRepository: GenericRepository<Book>,
    private val customerRepository: GenericRepository<Customer>
) {
    data class Input(val id: Int, val name: String?, val price: BigDecimal?, val status: BookStatus?, val customerId: Int?)
    fun update(input: Input) {
        val book = bookRepository.getById(input.id) ?: throw Exception("Book does not exist")
        val newCustomer = input.customerId?.let {
            customerRepository.getById(it) ?: throw Exception("New customer does not exist")
        } ?: book.customer
        val newBook = book.copy(
            name = input.name ?: book.name,
            status = input.status ?: book.status,
            price = input.price ?: book.price,
            customer = newCustomer
        )
        bookRepository.update(input.id, newBook)
    }
}
