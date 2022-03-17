package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.exceptions.Errors.ML101
import com.mercadolivro.core.use_cases.ports.GenericRepository
import java.math.BigDecimal

class GetBookDetails(private val bookRepository: GenericRepository<Book>) {
    data class Input(val id: Int)
    data class Output(
        val id: Int,
        val name: String,
        val price: BigDecimal,
        val status: BookStatus,
        val customerId: Int?
    ) {
        companion object {
            fun fromBookEntity(book: Book): Output {
                return Output(
                    id = book.id,
                    name = book.name,
                    price = book.price,
                    status = book.status,
                    customerId = book.customer?.id
                )
            }
        }
    }

    fun detail(input: Input): Output {
        val c = bookRepository.getById(input.id) ?: throw ML101.toResourceNotFoundException(input.id)
        return Output.fromBookEntity(c)
    }
}
