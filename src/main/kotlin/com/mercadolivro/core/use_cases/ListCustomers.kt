package com.mercadolivro.core.use_cases

import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

class ListCustomers(private val customerRepository: CustomerRepository) {
    data class Input(val name: String?, val paginationData: PaginationData)
    data class Output(val list: PaginatedResult<CreateCustomer.Output>)

    fun list(i: Input): Output {
        val all = if (i.name == null) {
            customerRepository.getAll(paginationData = i.paginationData)
        } else {
            customerRepository.getAllByName(i.name, paginationData = i.paginationData)
        }

        return Output(all.copyToAnotherType(CreateCustomer.Output::fromCustomer))
    }
}
