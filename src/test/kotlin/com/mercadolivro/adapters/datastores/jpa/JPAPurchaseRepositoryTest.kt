package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.*
import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.PurchaseRepository
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
internal class JPAPurchaseRepositoryTest {
    private lateinit var testingBook2: Book
    private lateinit var testingBook1: Book
    private lateinit var testingCustomer: Customer

    @Autowired
    private lateinit var sut: JPAPurchaseRepository

    @Autowired
    private lateinit var jpaBookRepository: JPABookRepository

    @Autowired
    private lateinit var jpaCustomerRepository: JPACustomerRepository

    @Autowired
    private lateinit var purchaseRecordRepository: PurchaseRecordRepository

    @Autowired
    private lateinit var bookRecordRepository: BookRecordRepository

    @Autowired
    private lateinit var customerRecordRepository: BookRecordRepository

    @BeforeEach
    fun setup() {
        purchaseRecordRepository.deleteAll()
        bookRecordRepository.deleteAll()
        customerRecordRepository.deleteAll()

        testingCustomer = jpaCustomerRepository.saveCustomer()
        testingBook1 = jpaBookRepository.saveBook()
        testingBook2 = jpaBookRepository.saveBook()
    }

    @Test
    fun `creates a purchase`() {
        val bookIds = setOf(testingBook1.id, testingBook2.id)
        val purchase = sut.create(PurchaseRepository.CreateInput(
            customerId = testingCustomer.id,
            bookIds = bookIds,
            price = testingBook1.price + testingBook2.price
        ))

        val purchaseRecord = purchaseRecordRepository.getById(purchase.id)

        assertEquals(bookIds, purchase.bookIds)
        assertEquals(testingCustomer.id, purchase.customerId)
        assertEquals(purchase.createdAt, purchaseRecord.createdAt)
        assertEquals(purchase.price, purchaseRecord.price)
        assertEquals(purchase.id, purchaseRecord.id)
        assertEquals(purchase.nfe, purchaseRecord.nfe)
    }

    @AfterEach
    fun tearDown() {
        purchaseRecordRepository.deleteAll()
        bookRecordRepository.deleteAll()
        customerRecordRepository.deleteAll()
    }
}