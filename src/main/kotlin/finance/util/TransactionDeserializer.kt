package finance.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import finance.model.Transaction

import java.io.IOException
import java.sql.Date
import java.text.DecimalFormat

class TransactionDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<Transaction>(vc) {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): Transaction {
        val node = jsonParser.codec.readTree<JsonNode>(jsonParser)

        val guid = node.get("guid").asText()
        val sha256 = node.get("sha256").asText()
        val accountType = node.get("accountType").asText()
        val accountNameOwner = node.get("accountNameOwner").asText()
        val description = node.get("description").asText()
        val category = node.get("category").asText()
        val notes = node.get("notes").asText()

        val amount = (DecimalFormat("#.##").format(node.get("amount").asDouble())).toDouble()
        val cleared = node.get("cleared").asInt()
        val reoccurring = node.get("reoccurring").asBoolean()
        val transactionDate = Date(node.get("transactionDate").asLong() * 1000)
        val dateUpdated = Date(node.get("dateUpdated").asLong() * 1000)
        val dateAdded = Date(node.get("dateAdded").asLong() * 1000)

        return Transaction(guid, accountType, accountNameOwner, transactionDate, description, category, amount, cleared, reoccurring, notes, dateUpdated, dateAdded, sha256)
    }
}
