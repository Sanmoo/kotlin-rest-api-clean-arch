package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer

fun JPACustomerRepository.saveCustomer(name: String? = null, email: String? = null): Customer {
    val testingCustomer = makeCustomer().let { it.copy(name = name ?: it.name, email = email ?: it.email) }
    return create(
        name = testingCustomer.name,
        status = testingCustomer.status,
        email = testingCustomer.email,
        roles = testingCustomer.roles,
        password = testingCustomer.password
    )
}

fun JPABookRepository.saveBook(customerId: Int? = null, name: String? = null, status: BookStatus? = null): Book {
    val testingBook = TestFactories.makeBook()
    return create(
        name = name ?: testingBook.name,
        price = testingBook.price,
        status = status ?: testingBook.status,
        customerId = customerId
    )
}
