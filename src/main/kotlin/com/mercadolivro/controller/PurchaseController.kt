package com.mercadolivro.controller

import com.mercadolivro.controller.dto.GetPurchaseResponse
import com.mercadolivro.controller.dto.PostPurchaseRequest
import com.mercadolivro.core.use_cases.Purchase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/purchases")
class PurchaseController(private val purchase: Purchase) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purchase(@RequestBody request: PostPurchaseRequest): GetPurchaseResponse {
        val purchaseOutput = purchase.purchase(
            Purchase.Input(
                customerId = request.customerId,
                bookIds = request.bookIds
            )
        )

        return GetPurchaseResponse(
            id = purchaseOutput.id,
            createdAt = purchaseOutput.createdAt,
            price = purchaseOutput.price,
            nfe = purchaseOutput.nfe,
            bookIds = purchaseOutput.books.map { it.id }.toSet(),
            customerId = purchaseOutput.customer.id
        )
    }
}
