package com.mercadolivro.core.use_cases.exceptions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ResourceNotFoundTest {
    @Test
    fun `exposes expected properties`() {
        val error = ResourceNotFound(message = "Message", code = "code")

        assertEquals("Message", error.message)
        assertEquals("code", error.code)
    }
}