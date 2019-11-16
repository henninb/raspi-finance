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
open class InsertTransactionProcessor : Processor {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var transactionService: TransactionService

    private var transactionFailure: Transaction? = null
    private val resultMessage = ResultMessage()
    private var resultString:String = ""

    @Throws(Exception::class)
    override fun process(exchange: Exchange) {
        try {
            val payload = exchange.`in`.getBody(String::class.java)
            val transaction: Transaction = JsonTransactionProcessor.mapper.readValue(payload, Transaction::class.java)

            transactionFailure = transaction
            transactionService.insertTransaction(transaction)
            logger.info("transaction inserted, guid=" + transaction.guid + " description=" + transaction.description)

            resultMessage.message = "Successfully processed add message."
            resultMessage.resultCode = 0
            resultMessage.guid = transaction.guid
            resultMessage.setDate(ZonedDateTime.now())
            resultString = mapper.writeValueAsString(resultMessage)

            //exchange.setProperty("guid", transaction.guid)
            exchange.`in`.body = transaction.toString()
        } catch (dive: DataIntegrityViolationException) {
            if (dive.message!!.contains("could not execute statement; SQL [n/a]; constraint [guid_idx]")) {
                resultMessage.message = "Failure to processed add message: " + "Exception: " + dive + " Exception message:" + dive.message
                resultMessage.resultCode = 202
                resultMessage.guid = transactionFailure!!.guid
                resultMessage.setDate(ZonedDateTime.now())
                resultString = mapper.writeValueAsString(resultMessage)
                logger.error(resultString)
                exchange.`in`.body = transactionFailure.toString()
            }
        } catch (e: Exception) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + e + " Exception message:" + e.message
            resultMessage.resultCode = 203
            resultMessage.setDate(ZonedDateTime.now())
            resultString = mapper.writeValueAsString(resultMessage)
            logger.error(resultString)
            exchange.`in`.body = transactionFailure.toString()
        }
        finally {
            logger.info("end InsertTransaction Processor")
        }
    }

    companion object {
        val mapper = ObjectMapper()
    }
}
