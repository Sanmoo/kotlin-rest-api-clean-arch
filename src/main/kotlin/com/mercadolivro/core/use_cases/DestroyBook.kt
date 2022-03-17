package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.use_cases.ports.GenericRepository

class DestroyBook(private val bookRepository: GenericRepository<Book>) {
    data class Input(val id: Int)

    fun destroy(i: Input) {
        bookRepository.deleteById(i.id)
    }
}
