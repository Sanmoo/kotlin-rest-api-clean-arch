package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.GenericRepository

class DestroyCustomer(private val customerRepository: GenericRepository<Customer>) {
    data class Input(val id: Int)

    fun destroy(i: Input) {
        customerRepository.deleteById(i.id)
    }
}
