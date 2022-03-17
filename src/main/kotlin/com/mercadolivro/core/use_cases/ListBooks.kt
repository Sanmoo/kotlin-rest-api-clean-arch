package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.ports.BookRepository

class ListBooks(private val bookRepository: BookRepository) {
    data class Input(val name: String? = null, val status: BookStatus? = null)
    data class Output(val list: List<CreateBook.Output>)

    fun list(i: Input): Output {
        val all = if (i.name != null && i.status != null) {
            bookRepository.getAllByNameAndStatus(i.name, i.status)
        } else if (i.name != null && i.status == null) {
            bookRepository.getAllByName(i.name)
        } else if (i.name == null && i.status != null) {
            bookRepository.getAllByStatus(i.status)
        } else {
            bookRepository.getAll()
        }

        return Output(all.map {
            CreateBook.Output(
                id = it.id,
                name = it.name,
                customerId = it.customer?.id,
                price = it.price,
                status = it.status
            )
        })
    }
}
