package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecord
import com.mercadolivro.adapters.datastores.jpa.records.BookRecordRepository
import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.use_cases.ports.BookRepository

class JPABookRepository(
    private val bookRecordRepository: BookRecordRepository
) : JPAGenericRepository<Book, BookRecord>(bookRecordRepository, BookRecord),
    BookRepository
