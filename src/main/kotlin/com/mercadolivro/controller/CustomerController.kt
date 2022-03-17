package com.mercadolivro.controller

import com.mercadolivro.controller.dto.CustomerDTO
import com.mercadolivro.controller.dto.PaginatedResponse
import com.mercadolivro.controller.utils.toPaginatedResponse
import com.mercadolivro.controller.utils.toPaginationData
import com.mercadolivro.core.use_cases.*
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
    fun list(
        @RequestParam name: String?,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): PaginatedResponse<CustomerDTO> {
        val paginationData = pageable.toPaginationData()
        val list = listCustomers.list(ListCustomers.Input(name, paginationData = paginationData)).list
        return list.copyToAnotherType(list.content.map {
            CustomerDTO(id = it.id, name = it.name, email = it.email, status = it.status)
        }).toPaginatedResponse()
    }

    @GetMapping("{id}")
    fun get(@PathVariable id: Int): CustomerDTO? {
        val detail = getCustomerDetails.detail(GetCustomerDetails.Input(id))
        return detail?.let {
            CustomerDTO(
                id = detail.id,
                email = detail.email,
                name = detail.name,
                status = detail.status
            )
        }
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @Valid @RequestBody c: CustomerDTO) {
        updateCustomer.update(UpdateCustomer.Input(id = id, name = c.name, email = c.email, status = c.status))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun destroy(@PathVariable id: Int) {
        destroyCustomer.destroy(DestroyCustomer.Input(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody c: CustomerDTO): CustomerDTO {
        return createCustomer.createCustomer(
            CreateCustomer.Input(name = c.name, email = c.email)
        ).let { c.copy(id = it.id) }
    }
}
