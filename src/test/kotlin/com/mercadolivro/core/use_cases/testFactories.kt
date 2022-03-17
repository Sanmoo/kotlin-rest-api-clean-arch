package com.mercadolivro.core.use_cases

import com.mercadolivro.core.entities.Customer
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer

fun makeCreateCustomerOutput(customer: Customer = makeCustomer()): CreateCustomer.Output {
    return CreateCustomer.Output.fromCustomer(customer)
}
