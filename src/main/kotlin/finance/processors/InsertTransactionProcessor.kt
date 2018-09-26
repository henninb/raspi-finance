package finance.processors

import com.fasterxml.jackson.databind.ObjectMapper
import finance.models.Transaction
import finance.pojos.ResultMessage
import finance.services.TransactionService
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
class InsertTransactionProcessor : Processor {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val transactionService: TransactionService? = null

    var transactionFailure: Transaction? = null
    val resultMessage = ResultMessage()
    var resultString:String = ""

    @Throws(Exception::class)
    override fun process(exchange: Exchange) {
        try {
            val transaction = exchange.`in`.getBody(Transaction::class.java)
            transactionFailure = transaction
            transactionService!!.insertTransaction(transaction)
            LOGGER.info("transaction insert: guild: " + transaction.guid + " description: " + transaction.description)

            resultMessage.message = "Successfully processed add message."
            resultMessage.resultCode = 0
            resultMessage.guid = transaction.guid!!
            resultMessage.setDate(ZonedDateTime.now())
            resultString = mapper.writeValueAsString(resultMessage)
            LOGGER.info(resultString)
            exchange.`in`.body = transaction.toString()
        } catch (dive: DataIntegrityViolationException) {
            if (dive.message!!.contains("could not execute statement; SQL [n/a]; constraint [guid_idx]")) {
                resultMessage.message = "Failure to processed add message: " + "Exception: " + dive + " Exception message:" + dive.message
                resultMessage.resultCode = 202
                resultMessage.guid = transactionFailure!!.guid!!
                resultMessage.setDate(ZonedDateTime.now())
                resultString = mapper.writeValueAsString(resultMessage)
                LOGGER.error(resultString)
                exchange.`in`.body = transactionFailure.toString()
            }
        } catch (e: Exception) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + e + " Exception message:" + e.message
            resultMessage.resultCode = 203
            resultMessage.guid = transactionFailure!!.guid!!
            resultMessage.setDate(ZonedDateTime.now())
            resultString = mapper.writeValueAsString(resultMessage)
            LOGGER.error(resultString)
            exchange.`in`.body = transactionFailure.toString()
        }
        finally {
            LOGGER.info("end InsertTransaction Processor")
        }
    }

    companion object {
        val mapper = ObjectMapper()
    }
}
