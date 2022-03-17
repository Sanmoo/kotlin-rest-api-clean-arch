package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.EntityRecordMapper
import com.mercadolivro.adapters.datastores.jpa.records.JPARecord
import com.mercadolivro.adapters.datastores.jpa.utils.toPageable
import com.mercadolivro.adapters.datastores.jpa.utils.toPaginatedResult
import com.mercadolivro.core.entities.Entity
import com.mercadolivro.core.use_cases.ports.GenericRepository
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData
import org.springframework.data.jpa.repository.JpaRepository

open class JPAGenericRepository<E : Entity, R : JPARecord<E>>(
    private val crudRepository: JpaRepository<R, Int>,
    private val recordMapper: EntityRecordMapper<E, R>,
) : GenericRepository<E> {
    override fun getAll(paginationData: PaginationData): PaginatedResult<E> {
        val findAll = crudRepository.findAll(paginationData.toPageable())
        return findAll.toPaginatedResult().copyToAnotherType { it.toEntity() }
    }

    override fun getById(id: Int): E? {
        return crudRepository.findById(id).map { it.toEntity() }.orElse(null)
    }

    override fun update(id: Int, e: E) {
        if (!crudRepository.existsById(id)) {
            throw Exception("Entity with id $id does not exist")
        }

        val record = recordMapper.fromEntity(e)
        crudRepository.save(record)
    }

    override fun deleteById(id: Int) {
        if (!crudRepository.existsById(id)) {
            throw Exception("Entity with id $id does not exist")
        }

        crudRepository.deleteById(id)
    }
}
