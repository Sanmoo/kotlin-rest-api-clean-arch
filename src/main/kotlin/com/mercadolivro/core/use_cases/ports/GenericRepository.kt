package com.mercadolivro.core.use_cases.ports

import com.mercadolivro.core.entities.Entity

interface GenericRepository<E : Entity> {
    fun getAll(paginationData: PaginationData): List<E>
    fun getById(id: Int): E?
    fun update(id: Int, e: E)
    fun deleteById(id: Int)
}
