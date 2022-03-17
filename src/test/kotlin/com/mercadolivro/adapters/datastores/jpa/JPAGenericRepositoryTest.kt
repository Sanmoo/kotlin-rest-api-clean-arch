package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.BookRecordRepository
import com.mercadolivro.core.entities.TestFactories.Companion.makeBook
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
internal class JPAGenericRepositoryTest {
    @Autowired
    private lateinit var sut: JPABookRepository

    @Autowired
    private lateinit var bookRecordRepository: BookRecordRepository

    @BeforeEach
    @AfterEach
    fun truncateTables() {
        bookRecordRepository.deleteAll()
    }

    @Test
    fun `updates an entity`() {
        // Arrange
        val testingBook1 = sut.saveBook()

        // Act
        val updatedBook = testingBook1.copy(name = "new Name")
        sut.update(testingBook1.id, updatedBook)

        // Assert
        val fromDatabase = sut.getById(testingBook1.id)
        assertEquals(updatedBook, fromDatabase)
    }

    @Test
    fun `throws an error if trying to update non existent entity`() {
        // Arrange
        val testingBook1 = makeBook(1)
        val updatedBook = testingBook1.copy(name = "new Name")

        // Act
        assertThrows<Exception> {
            sut.update(testingBook1.id, updatedBook)
        }
    }

    @Test
    fun `returns null if trying to get an entity by id that does not exist`() {
        assertNull(sut.getById(5))
    }

    @Test
    fun `deletes an entity by id`() {
        val book = sut.saveBook()
        sut.deleteById(book.id)
        assertNull(sut.getById(book.id))
    }

    @Test
    fun `throws an error if trying to delete an entity by id that does not exist`() {
        assertThrows<Exception> {
            sut.deleteById(5)
        }
    }
}