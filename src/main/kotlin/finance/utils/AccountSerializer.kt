package finance.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import finance.models.Account
import org.slf4j.LoggerFactory
import java.io.IOException

class AccountSerializer @JvmOverloads constructor(t: Class<Account>? = null) : StdSerializer<Account>(t) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(account: Account, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeStartObject()

        if( account.accountNameOwner != null ) {
            jgen.writeStringField("accountNameOwner", account.accountNameOwner)
        } else {
            jgen.writeStringField("accountNameOwner", "")
            logger.warn("account.accountNameOwner is null.")
        }
        if( account.accountType != null ) {
            jgen.writeStringField("accountType", account.accountType)
        } else {
            jgen.writeStringField("accountType", "")
            logger.warn("account.accountType is null.")
        }
        if( account.activeStatus != null ) {
            jgen.writeStringField("activeStatus", account.activeStatus)
        } else {
            jgen.writeStringField("activeStatus", "")
            logger.warn("account.activeStatus is null.")
        }

        if( account.moniker != null ) {
            jgen.writeStringField("moniker", account.moniker)
        } else {
            jgen.writeStringField("moniker", "")
            logger.warn("account.moniker is null.")
        }

        //jgen.writeStringField("totals", java.lang.Double.toString(account.totals))
        jgen.writeStringField("totals", account.totals.toString())
        jgen.writeStringField("totalsBalanced", account.totalsBalanced.toString())
        //TODO: error handle for the dates below.
        jgen.writeNumberField("dateClosed", account.dateClosed.time / 1000)
        jgen.writeNumberField("dateUpdated", account.dateUpdated.time / 1000)
        jgen.writeNumberField("dateAdded", account.dateAdded.time / 1000)
        jgen.writeEndObject()
    }
}