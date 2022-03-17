package com.mercadolivro.adapters.datastores.jpa.utils

import com.mercadolivro.core.use_cases.ports.PaginationData
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

fun PaginationData.toPageable(): Pageable {
    return PageRequest.of(this.pageNumber, this.pageSize)
}
