package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.Purchase
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.ports.AsynchronousCoordinator
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PurchaseRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class Purchase(
    private val purchaseRepository: PurchaseRepository,
    private val bookRepository: BookRepository,
    private val customerRepository: CustomerRepository,
    private val async: AsynchronousCoordinator,
) {
    data class Input(val customerId: Int, val bookIds: Set<Int>)

    data class Output(
        val id: Int,
        val customer: GetCustomerDetails.Output,
        val books: List<GetBookDetails.Output>,
        val nfe: String? = null,
        val price: BigDecimal,
        val createdAt: LocalDateTime
    ) {
        companion object {
            fun fromEntities(p: Purchase, c: Customer, bs: List<Book>): Output {
                return Output(
                    id = p.id,
                    customer = GetCustomerDetails.Output.fromCustomerEntity(c),
                    books = bs.map { GetBookDetails.Output.fromBookEntity(it) },
                    price = p.price,
                    createdAt = p.createdAt
                )
            }
        }
    }

    fun purchase(input: Input): Output {
        val books = bookRepository.getAllByIds(input.bookIds)

        if (books.size != input.bookIds.size) {
            val foundBookIds = books.map { it.id }
            throw Errors.ML103.toResourceNotFoundException(input.bookIds.subtract(foundBookIds))
        }

        if (books.any { it.status in listOf(BookStatus.SOLD, BookStatus.DELETED) }) {
            throw Errors.ML104.toOperationNotAllowed()
        }

        val customer = customerRepository.getById(input.customerId) ?: throw Errors.ML201.toResourceNotFoundException(input.customerId)

        val purchaseRepositoryCreateInput = PurchaseRepository.CreateInput(
            customerId = customer.id,
            price = books.sumOf { it.price },
            bookIds = input.bookIds
        )

        val purchase = purchaseRepository.create(purchaseRepositoryCreateInput)

        async.doAsync {
            val updatedPurchase = purchase.copy(nfe = UUID.randomUUID().toString())
            purchaseRepository.update(updatedPurchase.id, updatedPurchase)
            books.forEach {
                bookRepository.update(it.id, it.copy(status = BookStatus.SOLD))
            }
        }

        return Output.fromEntities(purchase, customer, books)
    }
}
