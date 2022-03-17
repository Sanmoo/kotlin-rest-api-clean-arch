package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecordRepository
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecordRepository
import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginationData
import com.mercadolivro.core.use_cases.ports.PaginationData
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
internal class JPABookRepositoryTest {
    private lateinit var allBooks: List<Book>
    private lateinit var paginationData: PaginationData
    private lateinit var nonesBook2: Book
    private lateinit var nonesBook1: Book
    private lateinit var marcosBook1: Book
    private lateinit var alexBook2: Book
    private lateinit var alexBook1: Book
    private lateinit var alex: Customer
    private lateinit var marcos: Customer

    @Autowired
    private lateinit var sut: JPABookRepository

    @Autowired
    private lateinit var jpaCustomerRepository: JPACustomerRepository

    @Autowired
    private lateinit var customerRecordRepository: CustomerRecordRepository

    @Autowired
    private lateinit var bookRecordRepository: BookRecordRepository

    @BeforeEach
    fun setup() {
        bookRecordRepository.deleteAll()
        customerRecordRepository.deleteAll()

        marcos = jpaCustomerRepository.saveCustomer("Marcos", "marcos@test.com")
        alex = jpaCustomerRepository.saveCustomer("Alex", "alex@test.com")
        alexBook1 = sut.saveBook(alex.id, name = "Book 1", status = BookStatus.SOLD)
        alexBook2 = sut.saveBook(alex.id, name = "Book 2", status = BookStatus.SOLD)
        marcosBook1 = sut.saveBook(marcos.id, name = "Book 1", status = BookStatus.SOLD)
        nonesBook1 = sut.saveBook(name = "Book 1", status = BookStatus.ACTIVE)
        nonesBook2 = sut.saveBook(name = "Book 2", status = BookStatus.DELETED)
        paginationData = makePaginationData()
        allBooks = listOf(alexBook1, alexBook2, marcosBook1, nonesBook1, nonesBook2)
    }

    @Test
    fun `get all`() {
        val result = sut.getAll(paginationData)
        assertEquals(allBooks.toSet(), result.content.toSet())
    }

    @Test
    fun `get all by status`() {
        val result = sut.getAllByStatus(BookStatus.DELETED, paginationData)
        assertEquals(setOf(nonesBook2), result.content.toSet())
    }

    @Test
    fun `get all by name and status`() {
        val result = sut.getAllByNameAndStatus(name = "Book 1", status = BookStatus.SOLD, paginationData)
        assertEquals(setOf(alexBook1, marcosBook1), result.content.toSet())
    }

    @Test
    fun `find by customer`() {
        val result = sut.findByCustomer(marcos, paginationData)
        assertEquals(setOf(marcosBook1), result.content.toSet())
    }

//    @Test
//    fun `loads expected properties from database`() {
//        val result = sut.findByCustomer(marcos, paginationData)
//        assertEquals(marcosBook1.customer?.id, result.content[0].customer?.id)
//        assertEquals(marcosBook1.status, result.content[0].status)
//        assertEquals(setOf(marcosBook1), result.content.toSet())
//    }

    @Test
    fun `updates in batch`() {
        val updatedBooks = listOf(alexBook1.copy(status = BookStatus.CANCELLED), alexBook2.copy(status = BookStatus.CANCELLED))
        sut.updateAll(updatedBooks)
        val result = sut.findByCustomer(alex, paginationData)
        assertEquals(updatedBooks.toSet(), result.content.toSet())
    }

    @Test
    fun `get all by ids`() {
        val result = sut.getAllByIds(listOf(nonesBook2.id, nonesBook1.id))
        assertEquals(setOf(nonesBook2, nonesBook1), result.toSet())
    }

    @Test
    fun `get all by name`() {
        val result = sut.getAllByName("Book 1", paginationData)
        assertEquals(setOf(nonesBook1, alexBook1, marcosBook1), result.content.toSet())
    }
}