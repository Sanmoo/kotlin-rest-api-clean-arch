package com.mercadolivro.adapters.datastores.jpa

import com.mercadolivro.adapters.datastores.jpa.records.EntityRecordMapper
import com.mercadolivro.adapters.datastores.jpa.records.JPARecord
import com.mercadolivro.core.entities.Entity
import com.mercadolivro.core.use_cases.ports.GenericRepository
import org.springframework.data.repository.CrudRepository

open class JPAGenericRepository<E: Entity, R: JPARecord<E>>(
    private val crudRepository: CrudRepository<R, Int>,
    private val recordMapper: EntityRecordMapper<E, R>,
) : GenericRepository<E> {
    override fun getAll(): List<E> {
        return crudRepository.findAll().map { it.toEntity() }
    }

    override fun getById(id: Int): E? {
        return crudRepository.findById(id).map{ it.toEntity() }.orElse(null)
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
