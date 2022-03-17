package com.mercadolivro.core.use_cases

import com.mercadolivro.core.use_cases.ports.CustomerRepository

class ListCustomers(private val customerRepository: CustomerRepository) {
    data class Input(val name: String?)
    data class Output(val list: List<CreateCustomer.Output>)

    fun listCustomers(i: Input): Output {
        val all = if (i.name == null) {
            customerRepository.getAll()
        } else {
            customerRepository.getAllByName(i.name)
        }

        return Output(all.map {
            CreateCustomer.Output(id = it.id ?: throw RuntimeException("Unexpected situation"), name = it.name, email = it.email)
        })
    }
}
