package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.ports.GenericRepository
import java.math.BigDecimal

class GetBookDetails(private val bookRepository: GenericRepository<Book>) {
    data class Input(val id: Int)
    data class Output(val id: Int, val name: String, val price: BigDecimal, val status: BookStatus, val customerId: Int?)

    fun detail(input: Input): Output? {
        val c = bookRepository.getById(input.id) ?: return null
        return Output(id = c.id, name = c.name, price = c.price, status = c.status, customerId = c.customer?.id)
    }
}
