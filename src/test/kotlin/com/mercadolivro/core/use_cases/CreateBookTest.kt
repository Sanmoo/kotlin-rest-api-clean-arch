package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.use_cases.ports.BookRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CreateBookTest {
    private lateinit var testingBookCustomer: Customer
    private lateinit var expectedResult: CreateBook.Output
    private lateinit var testingBook: Book

    @MockK
    private lateinit var bookRepository: BookRepository

    @InjectMockKs
    private lateinit var sut: CreateBook

    private fun setup(withCustomer: Boolean) {
        testingBookCustomer = makeCustomer()
        testingBook = makeBook().copy(customer = if (withCustomer) testingBookCustomer else null)
        every { bookRepository.create(
            name = testingBook.name,
            status = testingBook.status,
            customerId = if (withCustomer) testingBook.customer!!.id else null,
            price = testingBook.price
        ) } returns testingBook
        expectedResult = CreateBook.Output.fromEntity(testingBook)
    }

    @Test
    fun `creates a book and exposes expected properties in output`() {
        // Arrange
        setup(withCustomer = true)

        // Act
        val createInput = CreateBook.Input(testingBook.name, testingBook.price, testingBookCustomer.id)
        val result = sut.create(createInput)

        // Assert
        assertEquals(expectedResult, result)
        assertEquals(expectedResult.id, result.id)
        assertEquals(expectedResult.customerId, result.customerId)
        assertEquals(expectedResult.name, result.name)
        assertEquals(expectedResult.price, result.price)
        assertEquals(expectedResult.status, result.status)
    }

    @Test
    fun `creates a book without an associated customer`() {
        // Arrange
        setup(withCustomer = false)

        // Act
        val createInput = CreateBook.Input(testingBook.name, testingBook.price, null)
        val result = sut.create(createInput)

        // Assert
        assertEquals(expectedResult, result)
        assertEquals(null, result.customerId)
    }
}