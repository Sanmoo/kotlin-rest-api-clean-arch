package com.mercadolivro.core.ports

import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData

class TestFactories {
    companion object {
        fun <T> makePaginatedResult(paginationData: PaginationData, vararg t: T): PaginatedResult<T> {
            val totalRecords = t.size.toLong()
            val pageSize = paginationData.pageSize
            return PaginatedResult(
                content = listOf(*t),
                pageSize = pageSize,
                pageNumber = paginationData.pageNumber,
                totalPages = 1,
                totalRecords = totalRecords
            )
        }

        fun makePaginationData(): PaginationData {
            return PaginationData(10, 0)
        }
    }
}