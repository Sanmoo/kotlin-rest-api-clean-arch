package com.mercadolivro.core.use_cases.ports

data class PaginatedResult<T>(
    val content: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val totalPages: Int,
    val totalRecords: Long
) {
    fun <R> copyToAnotherType(mapContent: (i: T) -> R): PaginatedResult<R> {
        return PaginatedResult(
            content = this.content.map { mapContent(it) },
            pageSize = pageSize,
            pageNumber = pageNumber,
            totalPages = totalPages,
            totalRecords = totalRecords
        )
    }
}

