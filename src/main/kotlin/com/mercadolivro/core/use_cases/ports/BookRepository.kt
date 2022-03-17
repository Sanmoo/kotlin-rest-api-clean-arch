package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.CreateCustomer

interface BookRepository : GenericRepository<Book> {
}
