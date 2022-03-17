package com.mercadolivro.core.use_cases.ports

data class PaginatedResult<T>(
    val content: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val totalPages: Int,
    val totalRecords: Long
) {
    fun <R> copyToAnotherType(content: List<R>): PaginatedResult<R> {
        return PaginatedResult(
            content = content,
            pageSize = pageSize,
            pageNumber = pageNumber,
            totalPages = totalPages,
            totalRecords = totalRecords
        )
    }
}
