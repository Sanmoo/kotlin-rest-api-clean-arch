package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import java.math.BigDecimal

interface BookRepository : GenericRepository<Book> {
    fun create(name: String, price: BigDecimal, status: BookStatus, customerId: Int?): Book
    fun getAllByName(name: String): List<Book>
    fun getAllByStatus(status: BookStatus): List<Book>
    fun getAllByNameAndStatus(name: String, status: BookStatus): List<Book>
    fun findByCustomer(customer: Customer): List<Book>
    fun updateAll(books: List<Book>)
}
