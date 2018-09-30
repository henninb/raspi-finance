package finance.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import finance.models.Transaction

import java.io.IOException
import java.sql.Timestamp
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
        //System.out.println("transactionDate: " + node.get("transactionDate").asLong() * 1000)
        val transactionDate = Timestamp(node.get("transactionDate").asLong() * 1000)

        val dateUpdated = Timestamp(node.get("dateUpdated").asLong() * 1000)
        //System.out.println("dateUpdated: " + dateUpdated + " - " + node.get("dateUpdated").asLong() * 1000)
        val dateAdded = Timestamp(node.get("dateAdded").asLong() * 1000)
        //System.out.println("dateAdded: " + dateAdded + " - " +  node.get("dateAdded").asLong() * 1000)

        return Transaction(guid, accountType, accountNameOwner, transactionDate, description, category, amount, cleared, reoccurring, notes, dateUpdated, dateAdded, sha256)
    }
}
