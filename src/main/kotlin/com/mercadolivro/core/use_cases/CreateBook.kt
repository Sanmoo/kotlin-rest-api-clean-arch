package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.ports.BookRepository
import java.math.BigDecimal

class CreateBook(private val bookRepository: BookRepository) {
    data class Input(val name: String, val price: BigDecimal, val customerId: Int?)
    data class Output(val id: Int, val name: String, val price: BigDecimal, val status: BookStatus, val customerId: Int?) {
        companion object {
            fun fromEntity(book: Book): Output {
                return Output(id = book.id, name = book.name, price = book.price, status = book.status, customerId = book.customer?.id)
            }
        }
    }

    fun create(input: Input): Output {
        val book = bookRepository.create(name = input.name, price = input.price, status = BookStatus.ACTIVE, customerId = input.customerId)
        return Output.fromEntity(book)
    }
}
