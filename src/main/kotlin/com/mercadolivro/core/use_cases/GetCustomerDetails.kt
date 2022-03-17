package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import com.mercadolivro.core.use_cases.exceptions.Errors.ML201
import com.mercadolivro.core.use_cases.ports.CustomerRepository

class GetCustomerDetails(private val customerRepository: CustomerRepository) {
    data class Output(
        val id: Int,
        val email: String,
        val name: String,
        val status: CustomerStatus,
        val password: String,
        val roles: Set<Role>
    ) {
        companion object {
            fun fromCustomerEntity(c: Customer): Output {
                return Output(
                    id = c.id,
                    name = c.name,
                    email = c.email,
                    status = c.status,
                    password = c.password,
                    roles = c.roles
                )
            }
        }
    }

    fun detailById(id: Int): Output {
        val c = customerRepository.getById(id) ?: throw ML201.toResourceNotFoundException(id)
        return Output.fromCustomerEntity(c)
    }

    fun detailByEmail(email: String): Output {
        val c = customerRepository.getByEmail(email) ?: throw ML201.toResourceNotFoundException(email)
        return Output.fromCustomerEntity(c)
    }
}
