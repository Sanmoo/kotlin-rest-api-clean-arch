package com.mercadolivro.controller

import com.mercadolivro.controller.dto.CustomerDTO
import com.mercadolivro.core.use_cases.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
    private val createCustomer: CreateCustomer,
    private val listCustomers: ListCustomers,
    private val getCustomerDetails: GetCustomerDetails,
    private val updateCustomer: UpdateCustomer,
    private val destroyCustomer: DestroyCustomer,
) {
    @GetMapping
    fun list(@RequestParam name: String?): List<CustomerDTO> {
        return listCustomers.listCustomers(ListCustomers.Input(name)).list.map {
            CustomerDTO(id = it.id, name = it.name, email = it.email)
        }
    }

    @GetMapping("{id}")
    fun get(@PathVariable id: Int): CustomerDTO? {
        val detail = getCustomerDetails.detail(GetCustomerDetails.Input(id))
        return detail?.let { CustomerDTO(id = detail.id, email = detail.email, name = detail.name) }
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @Valid @RequestBody c: CustomerDTO) {
        updateCustomer.update(UpdateCustomer.Input(id = id, name = c.name, email = c.email))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun destroy(@PathVariable id: Int) {
        destroyCustomer.destroy(DestroyCustomer.Input(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody c: CustomerDTO): CustomerDTO
    {
        val output = createCustomer.createCustomer(
            CreateCustomer.Input(name = c.name, email = c.email)
        )
        return c.copy(id = output.id)
    }
}