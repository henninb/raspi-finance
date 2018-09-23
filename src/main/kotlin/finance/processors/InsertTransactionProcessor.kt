package finance.processors

import com.fasterxml.jackson.databind.ObjectMapper
import finance.model.Transaction
import finance.services.TransactionService
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class InsertTransactionProcessor : Processor {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    internal val jsonOutputBytes = ByteArrayOutputStream()

    @Autowired
    private val transactionService: TransactionService? = null

    @Throws(Exception::class)
    override fun process(exchange: Exchange) {
        var jsonString = ""
        try {
            val transactions = exchange.`in`.getBody(Array<Transaction>::class.java)
            for (transaction in transactions) {
                transactionService!!.insertTransaction(transaction)
            }
            mapper.writeValue(jsonOutputBytes, transactions)
            jsonString = String(jsonOutputBytes.toByteArray())
            exchange.`in`.body = jsonString
        } catch (dive: DataIntegrityViolationException) {
            if (dive.message!!.contains("could not execute statement; SQL [n/a]; constraint [guid_idx]")) {
                LOGGER.info("guid_idx SQL violation.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}
