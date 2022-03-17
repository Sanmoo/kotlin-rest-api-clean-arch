package com.mercadolivro

import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringApplication

internal class MercadoLivroApplicationKtTest {
    @Test
    fun testMain() {
        mockkStatic(SpringApplication::class)

        every { SpringApplication.run(any()) } returns mockk()
        main(arrayOf())
        verify (exactly = 1) { SpringApplication.run(MercadoLivroApplication::class.java) }

        unmockkStatic(SpringApplication::class)
    }
}