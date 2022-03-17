package com.mercadolivro.controller.dto

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class PostBookRequest(
    @field:NotEmpty
    val name: String,

    @field:NotNull
    val price: BigDecimal,

    @JsonAlias("customer_id")
    val customerId: Int?,
)
