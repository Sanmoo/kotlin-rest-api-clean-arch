package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecord
import com.mercadolivro.adapters.datastores.jpa.records.BookRecordRepository
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecord
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecordRepository
import com.mercadolivro.adapters.datastores.jpa.utils.toPageable
import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.PaginationData
import java.math.BigDecimal

class JPABookRepository(
    private val bookRecordRepository: BookRecordRepository,
    private val customerRecordRepository: CustomerRecordRepository,
) : JPAGenericRepository<Book, BookRecord>(bookRecordRepository, BookRecord),
    BookRepository {
    override fun create(name: String, price: BigDecimal, status: BookStatus, customerId: Int?): Book {
        val customer = customerId?.let { customerRecordRepository.findById(it).orElseThrow() }
        return bookRecordRepository.save(
            BookRecord(
                name = name,
                price = price,
                status = status,
                customer = customer
            )
        ).toEntity()
    }

    override fun getAllByName(name: String, paginationData: PaginationData): List<Book> {
        return bookRecordRepository.findAllByName(name, paginationData.toPageable())
            .map { it.toEntity() }
    }

    override fun getAllByStatus(status: BookStatus, paginationData: PaginationData): List<Book> {
        return bookRecordRepository.findAllByStatus(status, paginationData.toPageable())
            .map { it.toEntity() }
    }

    override fun getAllByNameAndStatus(name: String, status: BookStatus, paginationData: PaginationData): List<Book> {
        return bookRecordRepository.findAllByNameAndStatus(name, status, paginationData.toPageable())
            .map { it.toEntity() }
    }

    override fun findByCustomer(customer: Customer, paginationData: PaginationData): List<Book> {
        return bookRecordRepository.findByCustomer(
            CustomerRecord.fromEntity(customer),
            paginationData.toPageable()
        ).map { it.toEntity() }
    }

    override fun updateAll(books: List<Book>) {
        bookRecordRepository.saveAll(books.map { BookRecord.fromEntity(it) })
    }
}
