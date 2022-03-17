package com.mercadolivro.adapters.datastores.in_memory

import com.mercadolivro.core.entities.Entity
import com.mercadolivro.core.use_cases.ports.GenericRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

open class GenericInMemoryRepository<T : Entity> : GenericRepository<T> {
    protected val store = HashMap<Int, T>()
    protected var index = 1

    override fun getAll(paginationData: PaginationData): PaginatedResult<T> {
        TODO("Not implemented yet")
    }

    override fun getById(id: Int): T? {
        return store[id]
    }

    override fun update(id: Int, e: T) {
        store[id] = e
    }

    override fun deleteById(id: Int) {
        store.remove(id)
    }
}
