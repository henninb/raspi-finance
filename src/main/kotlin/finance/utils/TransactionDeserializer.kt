package finance.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import finance.models.Transaction
import finance.pojos.AccountType
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.sql.Date
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.Normalizer

class TransactionDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<Transaction>(vc) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val ascii = StandardCharsets.US_ASCII.newEncoder()

    private fun deserializeString(node: JsonNode?, name: String) : String {
        var str = ""
        try {
            str = node!!.get(name).asText()
            if( !ascii.canEncode(str) ) {
                logger.warn("invalid chars in $name")
            }
            str = Normalizer.normalize(str, Normalizer.Form.NFD)
                    .replace("[^\\p{ASCII}]".toRegex(), "")
                    .replace("^\\s+".toRegex(), "")
                    .replace("\\s+$".toRegex(), "")
                    .replace("\\s{2}+".toRegex(), " ")
        }
        catch(ex: Exception) {
            logger.warn("$name was null.")
        }
        return str
    }

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): Transaction {
        //var node : JsonNode = ObjectCodec().createObjectNode();
        var node: JsonNode? = null
        try {
            node = jsonParser.codec.readTree<JsonNode>(jsonParser)
        } catch(e :Exception) {
            logger.warn("node is null")
        }

        val guid = deserializeString(node,"guid")
        val sha256 = deserializeString(node,"sha256")
        val accountType = deserializeString(node,"accountType")

        val accountTypeEnum: AccountType = AccountType.valueOf(accountType)
        logger.info(accountTypeEnum.name)

        val accountNameOwner = deserializeString(node,"accountNameOwner")
        val description = deserializeString(node,"description")
        val category = deserializeString(node,"category")
        val notes = deserializeString(node,"notes")

        var amount = 0.00
        try {
            amount = (DecimalFormat("#.##").format(node!!.get("amount").asDouble())).toDouble()
        } catch (ex: Exception) {
            logger.warn("amount was null.")
        }

        var cleared = 2
        try {
            cleared = node!!.get("cleared").asInt()
        } catch (ex: Exception) {
            logger.warn("cleared was null.")
        }

        var reoccurring = false
        try {
            reoccurring = node!!.get("reoccurring").asBoolean()
        } catch( e: Exception) {
            logger.warn("reoccurring is null and is now set to false.")
        }

        var transactionDate = Date(0)
        try {
            transactionDate = Date(node!!.get("transactionDate").asLong() * 1000)
        } catch( e: Exception) {
            logger.warn("transactionDate is null and is now set to Date(0).")
        }

        var dateUpdated = Timestamp(0)
        try {
            dateUpdated = Timestamp(node!!.get("dateUpdated").asLong() * 1000)
        } catch( e: Exception) {
            logger.warn("dateUpdated is null and is now set to Timestamp(0).")
        }

        var dateAdded = Timestamp(0)
        try {
            dateAdded = Timestamp(node!!.get("dateAdded").asLong() * 1000)
        } catch( e: Exception) {
            logger.warn("dateAdded is null and is now set to Timestamp(0).")
        }

        return Transaction(guid, accountType, accountNameOwner, transactionDate, description, category, amount, cleared, reoccurring, notes, dateUpdated, dateAdded, sha256)
    }
}
