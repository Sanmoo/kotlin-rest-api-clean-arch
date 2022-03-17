package com.mercadolivro.controller.dto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LoginRequestTest {
    @Test
    fun `dto exposes expected properties`() {
        val lr = LoginRequest("email@test.com", "123456")
        assertEquals("email@test.com", lr.email)
        assertEquals("123456", lr.password)
    }
}