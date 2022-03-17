package com.mercadolivro.core.use_cases

import com.mercadolivro.core.use_cases.ports.BookRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DestroyBookTest {
    @MockK
    private lateinit var bookRepository: BookRepository

    @InjectMockKs
    private lateinit var sut: DestroyBook

    @Test
    fun `destroys a book`() {
        // Arrange
        every { bookRepository.deleteById(1) } just Runs

        // Act
        sut.destroy(DestroyBook.Input(1))

        // Assert
        verify(exactly = 1) { bookRepository.deleteById(1) }
    }
}