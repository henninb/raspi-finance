package finance.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import finance.models.Transaction
import org.slf4j.LoggerFactory
import java.io.IOException
import java.sql.Date
import java.sql.Timestamp
import java.text.DecimalFormat

class TransactionDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<Transaction>(vc) {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): Transaction {
        //var node : JsonNode = ObjectCodec().createObjectNode();
        var node: JsonNode? = null
        try {
            node = jsonParser.codec.readTree<JsonNode>(jsonParser)
        } catch(e :Exception) {
            LOGGER.warn("node is null")
        }

        var guid = ""
        try {
            guid = node!!.get("guid").asText()
        }
        catch(ex: Exception) {
            //TODO: generate a guid
            LOGGER.warn("guid was null")
        }

        var sha256 = ""
        try {
            sha256 = node!!.get("sha256").asText()
        }
        catch(ex: Exception) {
            LOGGER.warn("sha256 was null")
        }

        var accountType = ""
        try {
            accountType = node!!.get("accountType").asText()
        }
        catch(ex: Exception) {
            LOGGER.warn("accountType was null and set to default value of credit.")
        }

        val accountNameOwner = node!!.get("accountNameOwner").asText()
        if( accountNameOwner == null ) {
            LOGGER.warn("accountNameOwner is null is not cool")
        }

        val description = node.get("description").asText()
        if( description == null ) {
            LOGGER.warn("description is null is not cool")
        }
        var category = node.get("category").asText()
        if( category == null ) {
            LOGGER.warn("category is null and is now set to empty.")
            category = ""
        }

        var notes = node.get("notes").asText()
        if( notes == null ) {
            LOGGER.warn("notes is null and is now set to empty.")
            notes = ""
        }

        //TODO: tidy up these possible failures
        val amount = (DecimalFormat("#.##").format(node.get("amount").asDouble())).toDouble()
        val cleared = node.get("cleared").asInt()

        var reoccurring = false
        try {
            reoccurring = node.get("reoccurring").asBoolean()
        } catch( e: Exception) {
            LOGGER.warn("reoccurring is null and is now set to false.")
        }

        var transactionDate = Date(0)
        try {
            transactionDate = Date(node.get("transactionDate").asLong() * 1000)
        } catch( e: Exception) {
            LOGGER.warn("transactionDate is null and is now set to Date(0).")
        }

        var dateUpdated = Timestamp(0)
        try {
            dateUpdated = Timestamp(node.get("dateUpdated").asLong() * 1000)
        } catch( e: Exception) {
            LOGGER.warn("dateUpdated is null and is now set to Timestamp(0).")
        }

        var dateAdded = Timestamp(0)
        try {
            dateAdded = Timestamp(node.get("dateAdded").asLong() * 1000)
        } catch( e: Exception) {
            LOGGER.warn("dateAdded is null and is now set to Timestamp(0).")
        }

        return Transaction(guid, accountType, accountNameOwner, transactionDate, description, category, amount, cleared, reoccurring, notes, dateUpdated, dateAdded, sha256)
    }
}
