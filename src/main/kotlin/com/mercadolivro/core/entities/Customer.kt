package com.mercadolivro.core.entities

data class Customer(
    override val id: Int,
    val name: String,
    val email: String,
    val status: CustomerStatus,
    val password: String,
    val roles: Set<Role>,
) : Entity
