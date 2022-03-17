package com.mercadolivro.adapters

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ExtendWith(MockKExtension::class)
internal class BCryptEncryptorTest {
    @MockK
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @InjectMockKs
    private lateinit var sut: BCryptEncryptor

    @Test
    fun testEncrypt() {
        // Arrange
        every { bCryptPasswordEncoder.encode("teste") } returns "encoded"

        // Act
        val encrypted = sut.encrypt("teste")

        // Assert
        assertEquals("encoded", encrypted)
    }
}