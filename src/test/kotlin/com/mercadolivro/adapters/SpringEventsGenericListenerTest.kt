package com.mercadolivro.adapters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SpringEventsGenericListenerTest {
    @Test
    fun test() {
        // Arrange
        var called = false

        // Act
        SpringEventsGenericListener().handleEvent(SpringEventsAsynchronousCoordinator.GenericEvent(this) {
            called = true
        })

        // Assert
        assertTrue(called)
    }
}