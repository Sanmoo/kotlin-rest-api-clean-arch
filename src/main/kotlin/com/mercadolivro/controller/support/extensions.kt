package com.mercadolivro.controller.support

import com.mercadolivro.controller.dto.PaginatedResponse
import com.mercadolivro.core.use_cases.ports.PaginatedResult
import com.mercadolivro.core.use_cases.ports.PaginationData
import org.springframework.data.domain.Pageable

fun Pageable.toPaginationData(): PaginationData {
    return PaginationData(pageSize = this.pageSize, pageNumber = this.pageNumber)
}

fun <T> PaginatedResult<T>.toPaginatedResponse(): PaginatedResponse<T> {
    return PaginatedResponse(
        content = content,
        pageNumber = pageNumber,
        pageSize = pageSize,
        totalPages = totalPages,
        totalRecords = totalRecords
    )
}
