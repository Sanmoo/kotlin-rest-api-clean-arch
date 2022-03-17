package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Entity

interface JPARecord<E: Entity> {
    var id: Int?
    fun toEntity(): E
}
