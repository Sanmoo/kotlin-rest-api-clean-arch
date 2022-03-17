package com.mercadolivro.core.use_cases.ports

interface AsynchronousCoordinator {
    fun doAsync(function: () -> Unit)
}