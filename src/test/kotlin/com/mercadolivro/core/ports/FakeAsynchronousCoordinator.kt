package com.mercadolivro.core.ports

import com.mercadolivro.core.use_cases.ports.AsynchronousCoordinator

class FakeAsynchronousCoordinator : AsynchronousCoordinator {
    override fun doAsync(function: () -> Unit) {
        function()
    }
}