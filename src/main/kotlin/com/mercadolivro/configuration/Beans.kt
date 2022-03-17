package com.mercadolivro.configuration

import com.mercadolivro.adapters.BCryptEncryptor
import com.mercadolivro.adapters.SpringEventsAsynchronousCoordinator
import com.mercadolivro.adapters.SpringEventsGenericListener
import com.mercadolivro.adapters.datastores.jpa.JPABookRepository
import com.mercadolivro.adapters.datastores.jpa.JPACustomerRepository
import com.mercadolivro.adapters.datastores.jpa.JPAPurchaseRepository
import com.mercadolivro.controller.support.JWTUtil
import com.mercadolivro.controller.support.UserDetailsCustomService
import com.mercadolivro.core.use_cases.*
import org.apache.catalina.filters.CorsFilter
import org.springframework.context.support.beans
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

val beans = beans {
    bean<JPACustomerRepository>()
    bean<JPABookRepository>()
    bean<JPAPurchaseRepository>()
    bean<CreateCustomer>()
    bean<ListCustomers>()
    bean<GetCustomerDetails>()
    bean<UpdateCustomer>()
    bean<DestroyCustomer>()
    bean<CreateBook>()
    bean<GetBookDetails>()
    bean<ListBooks>()
    bean<UpdateBook>()
    bean<DestroyBook>()
    bean<Purchase>()
    bean<SpringEventsAsynchronousCoordinator>()
    bean<SpringEventsGenericListener>()
    bean<BCryptEncryptor>()
    bean<BCryptPasswordEncoder>()
    bean<UserDetailsCustomService>()
    bean<JWTUtil>()
}
