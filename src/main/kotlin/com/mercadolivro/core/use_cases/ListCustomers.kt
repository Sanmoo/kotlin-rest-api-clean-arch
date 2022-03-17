package com.mercadolivro.core.use_cases

import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PaginationData

class ListCustomers(private val customerRepository: CustomerRepository) {
    data class Input(val name: String?, val paginationData: PaginationData)
    data class Output(val list: List<CreateCustomer.Output>)

    fun list(i: Input): Output {
        val all = if (i.name == null) {
            customerRepository.getAll(paginationData = i.paginationData)
        } else {
            customerRepository.getAllByName(i.name, paginationData = i.paginationData)
        }

        return Output(all.map {
            CreateCustomer.Output(id = it.id, name = it.name, email = it.email, status = it.status)
        })
    }
}
