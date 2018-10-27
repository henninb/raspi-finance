package finance.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import finance.models.Account
import org.slf4j.LoggerFactory

import java.io.IOException
import java.sql.Timestamp
import java.text.DecimalFormat

class AccountDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<Account>(vc) {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): Account {
        val node = jsonParser.codec.readTree<JsonNode>(jsonParser)
        val accountNameOwner = node.get("accountNameOwner").asText()
        LOGGER.info("accountNameOwner: " + node.get("accountNameOwner").asText())
        val accountType = node.get("accountType").asText()
        LOGGER.info("accountType: " + node.get("accountType").asText())
        val activeStatus = node.get("activeStatus").asText()
        LOGGER.info("activeStatus: " + node.get("activeStatus").asText())
        val moniker = node.get("moniker").asText()
        val totals = (DecimalFormat("#.##").format(node.get("totals").asDouble())).toDouble()
        val totalsBalanced = (DecimalFormat("#.##").format(node.get("totalsBalanced").asDouble())).toDouble()
        val dateClosed = Timestamp(node.get("dateClosed").asLong() * 1000)
        val dateUpdated = Timestamp(node.get("dateUpdated").asLong() * 1000)
        val dateAdded = Timestamp(node.get("dateAdded").asLong() * 1000)

        return Account(accountNameOwner, accountType, activeStatus, moniker, totals, totalsBalanced, dateClosed, dateUpdated, dateAdded)
    }
}