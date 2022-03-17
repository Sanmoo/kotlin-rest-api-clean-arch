package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginatedResult
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginationData
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.OperationNotAllowed
import com.mercadolivro.core.use_cases.exceptions.ResourceNotFound
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DestroyCustomerTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookRepository: BookRepository

    @InjectMockKs
    private lateinit var destroyCustomer: DestroyCustomer

    private lateinit var testingCustomer: Customer
    private lateinit var testingBooks: List<Book>

    private fun setupCommonSteps() {
        // Arrange
        testingCustomer = makeCustomer()
        every { customerRepository.getById(testingCustomer.id) } returns testingCustomer
        val testingBook1 = makeBook().copy(customer = testingCustomer)
        val testingBook2 = makeBook().copy(customer = testingCustomer)
        testingBooks = listOf(testingBook1, testingBook2)
        val paginatedResultBooks = makePaginatedResult(makePaginationData(), testingBooks[0], testingBooks[1])
        every { bookRepository.findByCustomer(testingCustomer, any()) } returns paginatedResultBooks
        every { bookRepository.updateAll(any()) } just Runs
        every { customerRepository.update(any(), any()) } just Runs
    }

    @Test
    fun `update a customer and its books`() {
        // Arrange
        setupCommonSteps()

        // Act
        destroyCustomer.destroy(DestroyCustomer.Input(testingCustomer.id))

        // Assert
        verify(exactly = 1) {
            bookRepository.updateAll(testingBooks.map {
                it.copy(
                    customer = null,
                    status = BookStatus.DELETED
                )
            })
        }
        verify(exactly = 1) {
            customerRepository.update(
                testingCustomer.id,
                testingCustomer.copy(status = CustomerStatus.INACTIVE)
            )
        }
    }

    @Test
    fun `throws an error if tries to destroy a non existent customer`() {
        // Arrange
        setupCommonSteps()
        every { customerRepository.getById(testingCustomer.id) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            destroyCustomer.destroy(DestroyCustomer.Input(testingCustomer.id))
        }

        // Assert
        assertEquals(Errors.ML201.formatErrorCode(), error.code)
        verify(exactly = 0) { bookRepository.updateAll(any()) }
        verify(exactly = 0) { customerRepository.update(any(), any()) }
    }

    @Test
    fun `throws an error if tries to destroy an incative customer`() {
        // Arrange
        setupCommonSteps()
        every { customerRepository.getById(testingCustomer.id) } returns testingCustomer.copy(status = CustomerStatus.INACTIVE)

        // Act
        val error = assertThrows<OperationNotAllowed> {
            destroyCustomer.destroy(DestroyCustomer.Input(testingCustomer.id))
        }

        // Assert
        assertEquals(Errors.ML202.formatErrorCode(), error.code)
        verify(exactly = 0) { bookRepository.updateAll(any()) }
        verify(exactly = 0) { customerRepository.update(any(), any()) }
    }
}