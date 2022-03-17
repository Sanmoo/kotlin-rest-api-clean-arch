package com.mercadolivro.configuration

import com.mercadolivro.adapters.datastores.jpa.JPACustomerRepository
import com.mercadolivro.core.use_cases.*
import org.springframework.context.support.beans

val beans = beans {
    bean<JPACustomerRepository>()
    bean<CreateCustomer>()
    bean<ListCustomers>()
    bean<GetCustomerDetails>()
    bean<UpdateCustomer>()
    bean<DestroyCustomer>()
}
