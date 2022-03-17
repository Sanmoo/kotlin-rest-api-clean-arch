package com.mercadolivro.core.use_cases.exceptions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class OperationNotAllowedTest {
    @Test
    fun `exposes expected properties`() {
        // Act
        val error = OperationNotAllowed(reason = "Reason", code = "Code")

        // Assert
        assertEquals("Reason", error.reason)
        assertEquals("Code", error.code)
    }
}