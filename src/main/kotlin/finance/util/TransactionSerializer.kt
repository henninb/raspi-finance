package finance.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import finance.model.Transaction

import java.io.IOException

class TransactionSerializer @JvmOverloads constructor(t: Class<Transaction>? = null) : StdSerializer<Transaction>(t) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(transaction: Transaction, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeStartObject()
        jgen.writeStringField("guid", transaction.guid)
        jgen.writeStringField("sha256", transaction.sha256)
        jgen.writeStringField("accountType", transaction.accountType)
        jgen.writeStringField("accountNameOwner", transaction.accountNameOwner)
        jgen.writeStringField("description", transaction.description)
        jgen.writeStringField("category", transaction.category)
        jgen.writeStringField("notes", transaction.notes)
        jgen.writeNumberField("cleared", transaction.cleared)
        jgen.writeBooleanField("reoccurring", transaction.reoccurring)
        jgen.writeStringField("amount", java.lang.Double.toString(transaction.amount))
        jgen.writeNumberField("transactionDate", transaction.transactionDate!!.time / 1000)
        jgen.writeNumberField("dateUpdated", transaction.dateUpdated!!.time / 1000)
        jgen.writeNumberField("dateAdded", transaction.dateAdded!!.time / 1000)
        jgen.writeEndObject()
    }
}
