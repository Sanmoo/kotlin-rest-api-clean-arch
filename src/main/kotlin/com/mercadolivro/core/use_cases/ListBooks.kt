package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

class ListBooks(private val bookRepository: BookRepository) {
    data class Input(val name: String? = null, val status: BookStatus? = null, val paginationData: PaginationData)
    data class Output(val result: PaginatedResult<CreateBook.Output>)

    fun list(i: Input): Output {
        val all = if (i.name != null && i.status != null) {
            bookRepository.getAllByNameAndStatus(i.name, i.status, paginationData = i.paginationData)
        } else if (i.name != null && i.status == null) {
            bookRepository.getAllByName(i.name, paginationData = i.paginationData)
        } else if (i.name == null && i.status != null) {
            bookRepository.getAllByStatus(i.status, paginationData = i.paginationData)
        } else {
            bookRepository.getAll(i.paginationData)
        }

        return Output(
            all.copyToAnotherType(
                all.content.map {
                    CreateBook.Output(
                        id = it.id,
                        name = it.name,
                        customerId = it.customer?.id,
                        price = it.price,
                        status = it.status
                    )
                },
            )
        )
    }
}
