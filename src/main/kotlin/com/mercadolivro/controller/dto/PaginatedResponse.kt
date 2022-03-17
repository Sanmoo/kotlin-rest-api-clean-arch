package com.mercadolivro.controller.dto

import com.mercadolivro.core.use_cases.ports.PaginationData

class PaginatedResponse<T>(
    val content: List<T>,
    val paginationData: PaginationData
)
