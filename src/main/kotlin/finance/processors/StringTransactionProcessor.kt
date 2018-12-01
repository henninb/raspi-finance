package finance.processors

import finance.models.Transaction
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
open class StringTransactionProcessor : Processor {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Throws(Exception::class)
    override fun process(exchange: Exchange) {
        val transaction = exchange.`in`.getBody(Transaction::class.java)
        exchange.setProperty("guid", transaction.guid)
        exchange.`in`.body = transaction.toString()
    }
}
