package com.mercadolivro.adapters.datastores.jpa.records

import org.springframework.data.repository.CrudRepository

interface BookRecordRepository : CrudRepository <BookRecord, Int>
