package finance.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import finance.models.Account
import java.io.IOException

class AccountSerializer @JvmOverloads constructor(t: Class<Account>? = null) : StdSerializer<Account>(t) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(account: Account, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeStartObject()
        jgen.writeStringField("accountNameOwner", account.accountNameOwner)
        jgen.writeStringField("accountType", account.accountType)
        jgen.writeStringField("activeStatus", account.activeStatus)
        jgen.writeStringField("moniker", account.moniker)
        jgen.writeStringField("totals", java.lang.Double.toString(account.totals))
        jgen.writeStringField("totalsBalanced", java.lang.Double.toString(account.totalsBalanced))
        jgen.writeNumberField("dateClosed", account.dateClosed!!.time / 1000)
        jgen.writeNumberField("dateUpdated", account.dateUpdated!!.time / 1000)
        jgen.writeNumberField("dateAdded", account.dateAdded!!.time / 1000)
        jgen.writeEndObject()
    }
}