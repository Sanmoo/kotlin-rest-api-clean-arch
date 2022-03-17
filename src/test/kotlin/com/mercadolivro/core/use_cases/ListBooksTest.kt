package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Book
import com.mercadolivro.core.entities.BookStatus
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginatedResult
import com.mercadolivro.core.ports.TestFactories.Companion.makePaginationData
import com.mercadolivro.core.use_cases.ports.BookRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ListBooksTest {
    private lateinit var testingBook2: Book
    private lateinit var testingBook1: Book
    private lateinit var paginatedResult: PaginatedResult<Book>
    private lateinit var paginationData: PaginationData
    private lateinit var expectedOutput: ListBooks.Output

    @MockK
    private lateinit var bookRepository: BookRepository

    @InjectMockKs
    private lateinit var sut: ListBooks

    @BeforeEach
    fun setup() {
        testingBook1 = makeBook()
        testingBook2 = makeBook()
        paginationData = makePaginationData()
        paginatedResult = makePaginatedResult(paginationData, testingBook1, testingBook2)
        expectedOutput = ListBooks.Output(paginatedResult.copyToAnotherType { CreateBook.Output.fromEntity(it) })
    }

    @Test
    fun `list all books and expose expected properties in output`() {
        // Arrange
        every { bookRepository.getAll(paginationData) } returns paginatedResult

        // Act
        val result = sut.list(ListBooks.Input(paginationData = paginationData))

        // Assert
        assertEquals(expectedOutput, result)
        assertEquals(expectedOutput.result, result.result)
    }

    @Test
    fun `list all books by status`() {
        // Arrange
        every { bookRepository.getAllByStatus(status = BookStatus.CANCELLED, paginationData) } returns paginatedResult

        // Act
        val result = sut.list(ListBooks.Input(paginationData = paginationData, status = BookStatus.CANCELLED))

        // Assert
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `list all books by name`() {
        // Arrange
        every { bookRepository.getAllByName(name = "Test", paginationData) } returns paginatedResult

        // Act
        val result = sut.list(ListBooks.Input(paginationData = paginationData, name = "Test"))

        // Assert
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `list all books by name and status`() {
        // Arrange
        every {
            bookRepository.getAllByNameAndStatus(
                name = "Test",
                status = BookStatus.CANCELLED,
                paginationData
            )
        } returns paginatedResult

        // Act
        val result = sut.list(ListBooks.Input(paginationData = paginationData, name = "Test", status = BookStatus.CANCELLED))

        // Assert
        assertEquals(expectedOutput, result)
    }
}