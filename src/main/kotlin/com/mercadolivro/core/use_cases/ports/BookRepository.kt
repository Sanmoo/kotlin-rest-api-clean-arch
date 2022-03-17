package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import java.math.BigDecimal

interface BookRepository : GenericRepository<Book> {
    fun create(name: String, price: BigDecimal, status: BookStatus, customerId: Int): Book
}
