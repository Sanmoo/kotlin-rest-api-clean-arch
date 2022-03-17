package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Entity

interface JPARecord<E: Entity> {
    val id: Int?
    fun toEntity(): E
}
