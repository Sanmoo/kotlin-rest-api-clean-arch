package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.Errors.ML102
import com.mercadolivro.core.use_cases.ports.GenericRepository
import java.math.BigDecimal

class UpdateBook(
    private val bookRepository: GenericRepository<Book>,
    private val customerRepository: GenericRepository<Customer>
) {
    data class Input(val id: Int, val name: String?, val price: BigDecimal?, val status: BookStatus?, val customerId: Int?)
    fun update(input: Input) {
        val book = bookRepository.getById(input.id) ?: throw Errors.ML101.toResourceNotFoundException(input.id)

        if (book.status == BookStatus.DELETED) {
            throw ML102.toOperationNotAllowed(book.status)
        }

        val newCustomer = input.customerId?.let {
            customerRepository.getById(it) ?: throw Errors.ML201.toResourceNotFoundException(it)
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
