package com.mercadolivro.adapters.datastores.jpa.records

import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseRecordRepository : JpaRepository <PurchaseRecord, Int>
