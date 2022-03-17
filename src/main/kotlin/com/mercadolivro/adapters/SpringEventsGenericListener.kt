package com.mercadolivro.adapters

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

open class SpringEventsGenericListener {
    @Async
    @EventListener
    open fun handleEvent(event: SpringEventsAsynchronousCoordinator.GenericEvent) {
        event.function()
    }
}