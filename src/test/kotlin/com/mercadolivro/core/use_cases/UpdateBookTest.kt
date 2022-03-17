package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
internal class UpdateBookTest {
    private lateinit var newName: String
    private lateinit var newPrice: BigDecimal
    private lateinit var registeredBook: Book
    private lateinit var registeredCustomer2: Customer
    private lateinit var registeredCustomer1: Customer

    @MockK
    private lateinit var bookRepository: BookRepository

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    private lateinit var sut: UpdateBook

    @BeforeEach
    fun setup() {
        registeredCustomer1 = makeCustomer(1)
        registeredCustomer2 = makeCustomer(2)
        registeredBook = makeBook(1).copy(customer = registeredCustomer1)
        every { bookRepository.getById(registeredBook.id) } returns registeredBook
        every { customerRepository.getById(registeredCustomer1.id) } returns registeredCustomer1
        every { customerRepository.getById(registeredCustomer2.id) } returns registeredCustomer2
        every { bookRepository.update(registeredBook.id, any()) } just Runs
        newPrice = registeredBook.price + 5.toBigDecimal()
        newName = "${registeredBook.name} Updated"
    }

    @Test
    fun `updates a book name, price and customer`() {
        // Act
        sut.update(
            UpdateBook.Input(
                id = 1,
                price = newPrice,
                customerId = registeredCustomer2.id,
                status = null,
                name = newName
            )
        )

        // Assert
        verify(exactly = 1) {
            bookRepository.update(
                registeredBook.id,
                registeredBook.copy(price = newPrice, customer = registeredCustomer2, name = newName)
            )
        }
    }

    @Test
    fun `updates a book status`() {
        // Act
        sut.update(
            UpdateBook.Input(
                id = 1,
                price = null,
                customerId = null,
                status = BookStatus.CANCELLED,
                name = null
            )
        )

        // Assert
        verify(exactly = 1) {
            bookRepository.update(
                registeredBook.id,
                registeredBook.copy(status = BookStatus.CANCELLED)
            )
        }
    }

    @Test
    fun `throws a proper error when book cannot be found`() {
        // Arrange
        every { bookRepository.getById(registeredBook.id) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.update(
                UpdateBook.Input(
                    id = 1,
                    price = null,
                    customerId = null,
                    status = null,
                    name = null
                )
            )
        }

        // Assert
        assertEquals(Errors.ML101.formatErrorCode(), error.code)
    }

    @Test
    fun `throws a proper error when updating customer cannot be found`() {
        // Arrange
        every { customerRepository.getById(registeredCustomer1.id) } returns null
        every { customerRepository.getById(registeredCustomer2.id) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.update(
                UpdateBook.Input(
                    id = 1,
                    price = null,
                    customerId = registeredCustomer2.id,
                    status = null,
                    name = null
                )
            )
        }

        // Assert
        assertEquals(Errors.ML201.formatErrorCode(), error.code)
    }

    @Test
    fun `cannot update a deleted book`() {
        // Arrange
        every { bookRepository.getById(registeredBook.id) } returns registeredBook.copy(status = BookStatus.DELETED)

        // Act
        val error = assertThrows<OperationNotAllowed> {
            sut.update(
                UpdateBook.Input(
                    id = 1,
                    price = null,
                    customerId = null,
                    status = null,
                    name = null
                )
            )
        }

        // Assert
        assertEquals(Errors.ML102.formatErrorCode(), error.code)
    }
}