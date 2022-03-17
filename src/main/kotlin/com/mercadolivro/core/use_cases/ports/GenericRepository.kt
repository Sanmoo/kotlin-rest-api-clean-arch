package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Entity

interface GenericRepository<E : Entity> {
    fun create(e: E): Int
    fun getAll(): List<E>
    fun getById(id: Int): E?
    fun update(id: Int, e: E)
    fun deleteById(id: Int)
}
