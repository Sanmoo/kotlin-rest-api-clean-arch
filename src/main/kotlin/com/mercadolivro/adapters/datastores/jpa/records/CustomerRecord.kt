package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.CustomerStatus
import com.mercadolivro.core.entities.Role
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

    @Column
    var status: CustomerStatus? = null,

    @Column
    var password: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_roles", joinColumns = [JoinColumn(name = "customer_id")])
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    var roles: Set<Role> = setOf()
) : JPARecord<Customer> {
    companion object : EntityRecordMapper<Customer, CustomerRecord> {
        override fun fromEntity(input: Customer): CustomerRecord {
            return CustomerRecord(id = input.id, name = input.name, email = input.email, status = input.status)
        }
    }

    override fun toEntity(): Customer {
        return Customer(
            id = id ?: -1,
            name = name ?: "Could not be loaded",
            email = email ?: "Could not be loaded",
            status = status ?: throw Exception("Unexpected situation. Status should not be null in database"),
            password = password ?: "Could not be loaded",
            roles = roles
        )
    }
}
