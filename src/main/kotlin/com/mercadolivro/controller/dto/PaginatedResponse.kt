package com.mercadolivro.controller.dto

class PaginatedResponse<T>(
    val content: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val totalPages: Int,
    val totalRecords: Long
)
