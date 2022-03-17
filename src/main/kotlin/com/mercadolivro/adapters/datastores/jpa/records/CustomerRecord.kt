package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Customer
import javax.persistence.*

@Entity(name = "customers")
data class CustomerRecord  (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,

    @Column
    var name: String? = null,

    @Column
    var email: String? = null,
) : JPARecord<Customer> {
    companion object : EntityRecordMapper<Customer, CustomerRecord> {
        override fun fromEntity(input: Customer): CustomerRecord {
            return CustomerRecord(id = input.id, name = input.name, email = input.email)
        }
    }

    override fun toEntity(): Customer {
        return Customer(id = id ?: -1, name = name ?: "Could not be loaded", email = email ?: "Could not be loaded")
    }
}
