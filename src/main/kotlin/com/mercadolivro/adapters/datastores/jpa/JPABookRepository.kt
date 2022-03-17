package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecord
import com.mercadolivro.adapters.datastores.jpa.records.BookRecordRepository
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecordRepository
import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.ports.BookRepository
import java.math.BigDecimal

class JPABookRepository(
    private val bookRecordRepository: BookRecordRepository,
    private val customerRecordRepository: CustomerRecordRepository,
) : JPAGenericRepository<Book, BookRecord>(bookRecordRepository, BookRecord),
    BookRepository {
    override fun create(name: String, price: BigDecimal, status: BookStatus, customerId: Int): Book {
        val customer = customerRecordRepository.findById(customerId).orElseThrow()
        return bookRecordRepository.save(BookRecord(
            name = name,
            price = price,
            status = status,
            customer = customer
        )).toEntity()
    }
}
