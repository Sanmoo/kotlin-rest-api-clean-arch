package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecord
import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecordRepository
import com.mercadolivro.adapters.datastores.jpa.utils.toPageable
import com.mercadolivro.adapters.datastores.jpa.utils.toPaginatedResult
import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.use_cases.ports.CustomerRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

class JPACustomerRepository(
    private val customerRecordRepository: CustomerRecordRepository
) : JPAGenericRepository<Customer, CustomerRecord>(customerRecordRepository, CustomerRecord),
    CustomerRepository {
    override fun create(
        name: String,
        status: CustomerStatus,
        email: String,
        password: String,
        roles: Set<Role>
    ): Customer {
        return customerRecordRepository.save(
            CustomerRecord(
                name = name,
                email = email,
                status = status,
                password = password,
                roles = roles
            )
        ).toEntity()
    }

    override fun getAllByName(name: String, paginationData: PaginationData): PaginatedResult<Customer> {
        val findAllByName = customerRecordRepository.findAllByName(name, paginationData.toPageable())
        return findAllByName.toPaginatedResult().copyToAnotherType { it.toEntity() }
    }

    override fun existsByEmail(email: String): Boolean {
        return customerRecordRepository.existsByEmail(email)
    }

    override fun getByEmail(email: String): Customer? {
        return customerRecordRepository.findByEmail(email).map { it.toEntity() }.orElse(null)
    }
}
