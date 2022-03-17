package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
import javax.persistence.*

@Entity(name = "customers")
data class CustomerRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Int? = null,

    @Column
    private var name: String? = null,

    @Column
    private var email: String? = null,

    @Column
    private var status: CustomerStatus? = null,

    @Column
    private var password: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_roles", joinColumns = [JoinColumn(name = "customer_id")])
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    private var roles: Set<Role> = setOf()
) : JPARecord<Customer> {
    companion object : EntityRecordMapper<Customer, CustomerRecord> {
        override fun fromEntity(input: Customer): CustomerRecord {
            return CustomerRecord(id = input.id, name = input.name, email = input.email, status = input.status)
        }
    }

    override fun toEntity(): Customer {
        return Customer(
            id = id!!,
            name = name!!,
            email = email!!,
            status = status!!,
            password = password!!,
            roles = roles
        )
    }
}
