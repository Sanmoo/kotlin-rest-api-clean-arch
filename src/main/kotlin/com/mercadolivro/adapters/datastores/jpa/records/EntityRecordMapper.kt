package com.mercadolivro.adapters.datastores.jpa.records

import com.mercadolivro.core.entities.Entity

interface EntityRecordMapper<E : Entity, R> {
    fun fromEntity(input: E) : R
}
