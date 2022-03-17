package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer
import com.mercadolivro.core.entities.TestFactories.Companion.makePurchase
import com.mercadolivro.core.ports.FakeAsynchronousCoordinator
import com.mercadolivro.core.use_cases.exceptions.Errors
import com.mercadolivro.core.use_cases.exceptions.OperationNotAllowed
import com.mercadolivro.core.use_cases.exceptions.ResourceNotFound
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class PurchaseTest {
    private lateinit var expectedPurchaseRepositoryCreateInput: PurchaseRepository.CreateInput
    private lateinit var purchaseToBeCreated: com.mercadolivro.core.entities.Purchase
    private lateinit var registeredBookIds: Set<Int>
    private lateinit var registeredBook2: Book
    private lateinit var registeredBook1: Book
    private lateinit var registeredCustomer: Customer
    private lateinit var mockedUuid: UUID

    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var bookRepository: BookRepository

    @MockK
    private lateinit var customerRepository: CustomerRepository

    private lateinit var sut: Purchase

    @BeforeEach
    fun setup() {
        sut = Purchase(
            purchaseRepository = purchaseRepository,
            bookRepository = bookRepository,
            customerRepository = customerRepository,
            async = FakeAsynchronousCoordinator()
        )

        mockedUuid = UUID.randomUUID()
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns mockedUuid

        registeredCustomer = makeCustomer(1)
        registeredBook1 = makeBook(1)
        registeredBook2 = makeBook(2)
        registeredBookIds = setOf(registeredBook1.id, registeredBook2.id)
        every { bookRepository.getAllByIds(registeredBookIds) } returns listOf(registeredBook1, registeredBook2)
        every { customerRepository.getById(registeredCustomer.id) } returns registeredCustomer
        expectedPurchaseRepositoryCreateInput = PurchaseRepository.CreateInput(
            customerId = registeredCustomer.id,
            price = registeredBook1.price + registeredBook2.price,
            bookIds = registeredBookIds
        )
        purchaseToBeCreated = makePurchase(1).copy(customerId = registeredCustomer.id, bookIds = registeredBookIds)
        every { purchaseRepository.create(expectedPurchaseRepositoryCreateInput) } returns purchaseToBeCreated
        every {
            purchaseRepository.update(
                purchaseToBeCreated.id,
                purchaseToBeCreated.copy(nfe = mockedUuid.toString())
            )
        } just Runs
        every { bookRepository.update(registeredBook1.id, registeredBook1.copy(status = BookStatus.SOLD)) } just Runs
        every { bookRepository.update(registeredBook2.id, registeredBook2.copy(status = BookStatus.SOLD)) } just Runs
    }

    @AfterEach
    fun teardown() {
        unmockkStatic(UUID::class)
    }

    @Test
    fun `purchases a book`() {
        // Act
        val result = sut.purchase(Purchase.Input(customerId = registeredCustomer.id, bookIds = registeredBookIds))

        // Assert
        assertEquals(
            Purchase.Output.fromEntities(
                purchaseToBeCreated,
                registeredCustomer,
                listOf(registeredBook1, registeredBook2)
            ),
            result
        )
        verify(exactly = 1) {
            purchaseRepository.update(
                purchaseToBeCreated.id,
                purchaseToBeCreated.copy(nfe = mockedUuid.toString())
            )
        }
        verify(exactly = 1) {
            bookRepository.update(
                registeredBook1.id,
                registeredBook1.copy(status = BookStatus.SOLD)
            )
        }
        verify(exactly = 1) {
            bookRepository.update(
                registeredBook2.id,
                registeredBook2.copy(status = BookStatus.SOLD)
            )
        }
    }

    @Test
    fun `throws a proper error when some of the books cannot be found in datastore`() {
        // Arrange
        every { bookRepository.getAllByIds(bookIds = registeredBookIds) } returns listOf(registeredBook2)

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.purchase(
                Purchase.Input(
                    customerId = registeredCustomer.id,
                    bookIds = registeredBookIds
                )
            )
        }

        assertEquals(Errors.ML103.formatErrorCode(), error.code)
    }

    @Test
    fun `throws a proper error when trying to purchase an already sold book`() {
        // Arrange
        every { bookRepository.getAllByIds(registeredBookIds) } returns listOf(
            registeredBook1.copy(status = BookStatus.SOLD),
            registeredBook2
        )

        // Act
        val error = assertThrows<OperationNotAllowed> {
            sut.purchase(
                Purchase.Input(
                    customerId = registeredCustomer.id,
                    bookIds = registeredBookIds
                )
            )
        }

        assertEquals(Errors.ML104.formatErrorCode(), error.code)
    }

    @Test
    fun `throws a proper error when trying to purchase a deleted book`() {
        // Arrange
        every { bookRepository.getAllByIds(registeredBookIds) } returns listOf(
            registeredBook1,
            registeredBook2.copy(status = BookStatus.DELETED)
        )

        // Act
        val error = assertThrows<OperationNotAllowed> {
            sut.purchase(
                Purchase.Input(
                    customerId = registeredCustomer.id,
                    bookIds = registeredBookIds
                )
            )
        }

        assertEquals(Errors.ML104.formatErrorCode(), error.code)
    }

    @Test
    fun `throws a proper error when trying to do a purchase for a customer that cannot be found by the datastore`() {
        // Arrange
        every { customerRepository.getById(registeredCustomer.id) } returns null

        // Act
        val error = assertThrows<ResourceNotFound> {
            sut.purchase(
                Purchase.Input(
                    customerId = registeredCustomer.id,
                    bookIds = registeredBookIds
                )
            )
        }

        assertEquals(Errors.ML201.formatErrorCode(), error.code)
    }
}