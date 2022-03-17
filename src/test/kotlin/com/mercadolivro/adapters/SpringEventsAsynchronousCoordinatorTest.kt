package com.mercadolivro.adapters

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
internal class SpringEventsAsynchronousCoordinatorTest {
    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var sut: SpringEventsAsynchronousCoordinator

    @Test
    fun testDoAsync() {
        // Arrange
        val capturedEvent = slot<SpringEventsAsynchronousCoordinator.GenericEvent>()
        every { applicationEventPublisher.publishEvent(capture(capturedEvent)) } just Runs
        val lambda = {}

        // Act
        sut.doAsync(lambda)
        val captured = capturedEvent.captured

        // Assert
        verify(exactly = 1) { applicationEventPublisher.publishEvent( any() ) }
        assertEquals(lambda, captured.function)
        assertEquals(sut, captured.source)
    }
}