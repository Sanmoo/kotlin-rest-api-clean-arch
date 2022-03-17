package com.mercadolivro.adapters

import com.mercadolivro.core.use_cases.ports.AsynchronousCoordinator
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher

class SpringEventsAsynchronousCoordinator(
    private val applicationEventPublisher: ApplicationEventPublisher
) : AsynchronousCoordinator {
    override fun doAsync(function: () -> Unit) {
        applicationEventPublisher.publishEvent(GenericEvent(this, function))
    }

    class GenericEvent (
        source: Any,
        val function: () -> Unit
    ) : ApplicationEvent(source)
}