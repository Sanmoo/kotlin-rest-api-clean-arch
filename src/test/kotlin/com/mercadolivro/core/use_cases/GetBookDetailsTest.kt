package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.ResourceNotFound
import com.mercadolivro.core.use_cases.ports.BookRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetBookDetailsTest {
    private lateinit var testingBook: Book
    private lateinit var expectedResult: GetBookDetails.Output

    @MockK
    private lateinit var bookRepository: BookRepository

    @InjectMockKs
    private lateinit var sut: GetBookDetails

    private fun setup(withCustomer: Boolean) {
        val testingCustomer = makeCustomer()
        testingBook = makeBook().copy(customer = if (withCustomer) testingCustomer else null)
        every { bookRepository.getById(testingBook.id) } returns testingBook
        expectedResult = GetBookDetails.Output.fromBookEntity(testingBook)
    }

    @Test
    fun `retrieves a book without an associated customer and exposes expected properties in Output`() {
        // Arrange
        setup(withCustomer = false)

        // Act
        val result = sut.detail(GetBookDetails.Input(testingBook.id))

        // Assert
        assertEquals(expectedResult, result)
        assertEquals(expectedResult.customerId, result.customerId)
        assertEquals(expectedResult.id, result.id)
        assertEquals(expectedResult.name, result.name)
        assertEquals(expectedResult.status, result.status)
        assertEquals(expectedResult.price, result.price)
    }

    @Test
    fun `retrieves a book with an associated customer`() {
        // Arrange
        setup(withCustomer = true)

        // Act
        val result = sut.detail(GetBookDetails.Input(testingBook.id))

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `throws a proper error if the customer cannot be found`() {
        // Arrange
        setup(withCustomer = false)
        every { bookRepository.getById(testingBook.id) } returns null

        // Act & Assert
        val error = assertThrows<ResourceNotFound> {
            sut.detail(GetBookDetails.Input(testingBook.id))
        }
        assertEquals(Errors.ML101.formatErrorCode(), error.code)
    }
}