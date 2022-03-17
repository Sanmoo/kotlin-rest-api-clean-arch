package com.mercadolivro.adapters.datastores.jpa.utils

import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

fun PaginationData.toPageable(): Pageable {
    return PageRequest.of(this.pageNumber, this.pageSize)
}

fun <T> Page<T>.toPaginatedResult(): PaginatedResult<T> {
    return PaginatedResult(
        content = this.content,
        pageSize = this.size,
        pageNumber = this.number,
        totalPages = this.totalPages,
        totalRecords = this.totalElements
    )
}
