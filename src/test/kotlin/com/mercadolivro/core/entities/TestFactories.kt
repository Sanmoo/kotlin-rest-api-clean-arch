package com.mercadolivro.core.entities

import java.time.LocalDateTime

class TestFactories {
    companion object {
        private var iteratingId = 1

        fun makeCustomer(id: Int = iteratingId++): Customer {
            return Customer(
                id = id,
                name = "Testing Name",
                email = "testing-email${id}@test.com",
                status = CustomerStatus.ACTIVE,
                password = "123456",
                roles = setOf(Role.CUSTOMER)
            )
        }

        fun makeBook(id: Int = iteratingId++): Book {
            return Book(
                id = id,
                name = "Testing Name",
                status = BookStatus.ACTIVE,
                price = 50.05.toBigDecimal(),
                customer = null
            )
        }

        fun makePurchase(id: Int = iteratingId++): Purchase {
            return Purchase(
                id = id,
                customerId = 1,
                bookIds = setOf(1, 2),
                nfe = null,
                price = 0.05.toBigDecimal(),
                createdAt = LocalDateTime.of(2022, 3, 5, 18, 26, 16, 0)
            )
        }
    }
}