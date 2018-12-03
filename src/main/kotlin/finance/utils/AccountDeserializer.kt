package finance.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import finance.models.Account
import org.slf4j.LoggerFactory

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.Normalizer
import java.util.*

class AccountDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<Account>(vc) {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    private val ASCII = StandardCharsets.US_ASCII.newEncoder()

    fun deserializeString(node: JsonNode?, name: String) : String {
        var str = ""
        try {
            str = node!!.get(name).asText()
            if( !ASCII.canEncode(str) ) {
                LOGGER.warn("invalid chars in " + name)
            }
            str = Normalizer.normalize(str, Normalizer.Form.NFD)
                    .replace("[^\\p{ASCII}]".toRegex(), "")
                    .replace("^\\s+".toRegex(), "")
                    .replace("\\s+$".toRegex(), "")
                    .replace("\\s{2}+".toRegex(), " ")
        }
        catch(ex: Exception) {
            LOGGER.warn(name + " was null.")
        }
        return str
    }

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): Account {
        var node: JsonNode? = null
        try {
            node = jsonParser.codec.readTree<JsonNode>(jsonParser)
        } catch(e :Exception) {
            LOGGER.warn("node is null")
        }

        //val node = jsonParser.codec.readTree<JsonNode>(jsonParser)
        val accountNameOwner = deserializeString(node,"accountNameOwner")
        val accountType = deserializeString(node,"accountType")
        val activeStatus = deserializeString(node,"activeStatus")
        val moniker = deserializeString(node,"moniker")

        var totals = 0.00
        try {
            totals = (DecimalFormat("#.##").format(node!!.get("totals").asDouble())).toDouble()
        } catch (ex: Exception) {
            LOGGER.warn("totals was null.")
        }

        var totalsBalanced = 0.00
        try {
            totalsBalanced = (DecimalFormat("#.##").format(node!!.get("totalsBalanced").asDouble())).toDouble()
        } catch (ex: Exception) {
            LOGGER.warn("totalsBalanced was null.")
        }

        var dateClosed = Timestamp(0)
        try {
            dateClosed = Timestamp(node!!.get("dateClosed").asLong() * 1000)
        } catch( e: Exception) {
            LOGGER.warn("dateClosed is null and is now set to Timestamp(0).")
        }

        var dateUpdated = Timestamp(0)
        try {
            dateUpdated = Timestamp(node!!.get("dateUpdated").asLong() * 1000)
        } catch( e: Exception) {
            LOGGER.warn("dateUpdated is null and is now set to Timestamp(0).")
        }

        var dateAdded = Timestamp(0)
        try {
            dateAdded = Timestamp(node!!.get("dateAdded").asLong() * 1000)
        } catch( e: Exception) {
            LOGGER.warn("dateAdded is null and is now set to Timestamp(0).")
        }
        return Account(accountNameOwner, accountType, activeStatus, moniker, totals, totalsBalanced, dateClosed, dateUpdated, dateAdded)
    }
}