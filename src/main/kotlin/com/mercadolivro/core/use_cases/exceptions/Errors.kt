package com.mercadolivro.core.use_cases.exceptions

enum class Errors(private val message: String) {
    ML101("Book %s does not exist"),
    ML102("Cannot update a book with status %s"),
    ML201("Customer %s does not exist"),
    ML202("User is already not active"),
    ML203("It is not possible to update an INACTIVE customer"),
    ;

    fun toResourceNotFoundException(vararg args: Any): ResourceNotFound {
        return ResourceNotFound(message = message.format(*args), code = formatErrorCode())
    }

    fun toOperationNotAllowed(vararg args: Any): OperationNotAllowed {
        return OperationNotAllowed(reason = message.format(*args), code = formatErrorCode())
    }

    private fun formatErrorCode(): String {
        val asString = toString()
        return "${asString.subSequence(0, 2)}-${asString.subSequence(2, asString.length)}"
    }
}
