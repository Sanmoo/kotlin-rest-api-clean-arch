package com.mercadolivro.core.entities

data class Customer(override var id: Int? = null, val name: String, val email: String) : Entity
