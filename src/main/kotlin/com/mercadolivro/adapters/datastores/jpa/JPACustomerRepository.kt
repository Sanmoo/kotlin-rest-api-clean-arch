package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecord
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecordRepository
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.use_cases.ports.CustomerRepository

class JPACustomerRepository(
    private val customerRecordRepository: CustomerRecordRepository
) : JPAGenericRepository<Customer, CustomerRecord>(customerRecordRepository, CustomerRecord),
    CustomerRepository {

    override fun getAllByName(name: String): List<Customer> {
        return customerRecordRepository.findAllByName(name).map { it.toEntity() }
    }
}
