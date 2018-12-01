package finance.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import finance.models.Transaction
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.StandardCharsets

class TransactionSerializer @JvmOverloads constructor(t: Class<Transaction>? = null) : StdSerializer<Transaction>(t) {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    private val ASCII = StandardCharsets.US_ASCII.newEncoder()

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(transaction: Transaction, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeStartObject()
        if( transaction.guid != null ) {
            jgen.writeStringField("guid", transaction.guid)
        } else {
            jgen.writeStringField("guid", "")
            LOGGER.warn("transaction.guid is null.")
        }
        if( transaction.sha256 != null ) {
            jgen.writeStringField("sha256", transaction.sha256)
        } else {
            jgen.writeStringField("sha256", "")
            LOGGER.warn("transaction.sha256 is null.")
        }
        if( transaction.accountType != null ) {
            jgen.writeStringField("accountType", transaction.accountType)
        } else {
            jgen.writeStringField("accountType", "")
            LOGGER.warn("transaction.accountType is null.")
        }
        if( transaction.accountNameOwner != null ) {
            jgen.writeStringField("accountNameOwner", transaction.accountNameOwner)
        } else {
            jgen.writeStringField("accountNameOwner", "")
            LOGGER.warn("transaction.accountNameOwner is null.")
        }

        if( transaction.description != null ) {
            if( ASCII.canEncode(transaction.description) ) {
                jgen.writeStringField("description", transaction.description)
            } else {
                LOGGER.warn("description has invaid chars: " + transaction.description)
                jgen.writeStringField("description", transaction.description)
            }
        } else {
            jgen.writeStringField("description", "")
            LOGGER.warn("transaction.description is null.")
        }

        if( transaction.category != null ) {
            jgen.writeStringField("category", transaction.category)
        } else {
            jgen.writeStringField("category", "")
            LOGGER.warn("transaction.category is null.")
        }

        if( transaction.notes != null ) {
            if( ASCII.canEncode(transaction.notes) ) {
                jgen.writeStringField("notes", transaction.notes)
            } else {
                LOGGER.warn("notes has invaid chars: " + transaction.notes)
                jgen.writeStringField("notes", transaction.notes)
            }
        } else {
            jgen.writeStringField("notes", "")
            LOGGER.warn("transaction.notes is null.")
        }

        if( transaction.notes != null ) {
            jgen.writeNumberField("cleared", transaction.cleared)
        } else {
            jgen.writeNumberField("cleared", 0)
            LOGGER.warn("transaction.cleared is null.")
        }
        jgen.writeBooleanField("reoccurring", transaction.reoccurring)
        //jgen.writeStringField("amount", java.lang.Double.toString(transaction.amount))
        jgen.writeStringField("amount",  "%.2f".format(transaction.amount))
        jgen.writeNumberField("transactionDate", transaction.transactionDate.time / 1000)
        jgen.writeNumberField("dateUpdated", transaction.dateUpdated.time / 1000)
        jgen.writeNumberField("dateAdded", transaction.dateAdded.time / 1000)
        jgen.writeEndObject()
    }
}
