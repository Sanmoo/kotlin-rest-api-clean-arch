package com.mercadolivro.controller.utils

import com.mercadolivro.core.use_cases.ports.PaginationData
import org.springframework.data.domain.Pageable

fun Pageable.toPaginationData(): PaginationData {
    return PaginationData(pageSize = this.pageSize, pageNumber = this.pageNumber)
}
