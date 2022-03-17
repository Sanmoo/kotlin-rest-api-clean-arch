import com.mercadolivro.adapters.datastores.jpa.records.CustomerRecord
import com.mercadolivro.core.entities.TestFactories.Companion.makeCustomer

fun buildCustomerRecord(): CustomerRecord {
    val testCustomer = makeCustomer()
    return CustomerRecord.fromEntity(testCustomer)
}