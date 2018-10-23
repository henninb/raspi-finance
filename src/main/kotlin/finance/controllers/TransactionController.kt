package finance.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import finance.models.Transaction
import finance.services.TransactionService
import finance.pojos.ResultMessage
import finance.services.TransactionServiceInterfaceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping


@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@RestController
@RequestMapping("/transactions")
class TransactionController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var transactionService: TransactionService? = null

    //insert into t_transaction(account_type, account_name_owner, transaction_date, description, category, amount, is_cleared, notes, date_updated, date_added) VALUES('credit', 'usbankcash_brian', '8/1/2017', 'Mario Kart', 'toys', '49.99', false, '', '8/1/2017', '8/1/2017')
    //localhost:8080/transactions/findall?pageNumber=1&pageSize=20
    //localhost:8080/transactions/findall?limit=1&offset=1
    @GetMapping(value = "/findall")
    //@RequestMapping(value = "/users/get", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    //@RequestMapping(method = RequestMethod.GET, path = "pageable",  produces = MediaType.APPLICATION_JSON_VALUE)
    fun findAllTransactions(@RequestParam pageNumber: Int, @RequestParam pageSize: Int, pageable: Pageable): Page<Transaction> {

        var  pageable1: Pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC)
        //var  pageable1: Pageable = PageRequest.of(20,1,Sort.Direction.ASC,"country");

        return transactionService!!.findAllTransactions(pageable1)
    }

    //fun transactionFindAll(@RequestParam pageNumber: Int, @RequestParam pageSize: Int, response: HttpServletResponse): Page<Transaction> {
    //fun transactionFindAll(@RequestParam pageNumber: Int, @RequestParam pageSize: Int) :Page<Transaction> {

    //http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @GetMapping(value = "/select/{guid}")
    fun findtTransaction(@PathVariable guid: String): Transaction {
        val transaction: Transaction
        LOGGER.info("getTransaction=" + guid)
        transaction = transactionService!!.findByGuid(guid)
        if (transaction.accountNameOwner != "" ) {
            return transaction
        } else {
            return Transaction()
        }
    }

    //http://localhost:8080/transactions/update/<some_guid>
    @PutMapping(path = arrayOf("/update/{guid}"), consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun updateTransaction(@RequestBody transaction: Transaction, @PathVariable guid: String ) {
        val transaction = transactionService!!.findByGuid(guid)

    }

    //http://localhost:8080/transactions/insert
    @PostMapping(path = arrayOf("/insert"), consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun insertTransaction(@RequestBody transaction: Transaction) : String {
        val resultMessage = ResultMessage()
        var resultString: String = ""

        try {
            transactionService?.insertTransaction(transaction)
            resultMessage.message = "Successfully processed add message."
            resultMessage.resultCode = 0
            resultMessage.guid = transaction.guid!!
            resultMessage.setDate(ZonedDateTime.now())

            resultString = mapper.writeValueAsString(resultMessage)
            return resultString
        } catch (jpe: JsonProcessingException) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + jpe + " Exception message:" + jpe.message
            resultMessage.resultCode = 200
            resultString = mapper.writeValueAsString(resultMessage)
            return resultString
        } catch (e: Exception) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + e + " Exception message:" + e.message
            resultMessage.resultCode = 201
            resultString = mapper.writeValueAsString(resultMessage)
            return resultString
        }
    }

    //http://localhost:8080/transactions/deleteTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @GetMapping(value = "/delete/{guid}")
    fun deleteTransaction(@PathVariable guid: String): String {
        val restResult = ResultMessage()
        var resultString : String = ""
        restResult.guid = guid

        LOGGER.info(guid)
        try {
            val transaction = transactionService!!.findByGuid(guid)
            LOGGER.info(transaction.description)
            LOGGER.info(transaction.accountNameOwner)
            LOGGER.info(transaction.guid)
            LOGGER.info("transactions.contains(transaction) is found.")

            transactionService!!.deleteByGuid(guid)
            restResult.message = "Successfully processed delete message."
            restResult.resultCode = 0
            restResult.setDate(ZonedDateTime.now())
            resultString = mapper.writeValueAsString(restResult)
            //return resultString
        } catch (ex: Exception) {
            LOGGER.info(ex.message)
            restResult.message = "Failure to processed delete message: " + "Exception: " + ex + " Exception message:" + ex.message
            restResult.resultCode = 100
            restResult.setDate(ZonedDateTime.now())
            resultString = mapper.writeValueAsString(restResult)
            //return resultString
        } finally {
            return resultString
        }
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}
