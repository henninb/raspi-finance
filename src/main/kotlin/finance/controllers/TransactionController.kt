package finance.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import finance.models.Transaction
import finance.services.TransactionService
import finance.pojos.ResultMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.UUID

@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@RestController
@RequestMapping("/transactions")
class TransactionController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var transactionService: TransactionService? = null

    //insert into t_transaction(account_type, account_name_owner, transaction_date, description, category, amount, is_cleared, notes, date_updated, date_added) VALUES('credit', 'usbankcash_brian', '8/1/2017', 'Mario Kart', 'toys', '49.99', false, '', '8/1/2017', '8/1/2017')
    //http://localhost:8080/transactions/transactionFindAll
    @GetMapping(value = "/transactionFindAll")
    fun transactionFindAll(): List<Transaction> {
        return transactionService!!.findAll()
    }

    //http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @GetMapping(value = "/getTransaction/{guid}")
    fun getTransaction(@PathVariable guid: String): Transaction {
        val transaction: Transaction
        LOGGER.info(guid)
        //Transaction transaction = transactionRepository.findByGuid(guid);
        transaction = transactionService!!.findByGuid(guid)
        return transaction
    }

    //http://localhost:8080/transactions/insertTransaction
    @PostMapping(path = arrayOf("/insertTransaction"), consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun addMember(@RequestBody transaction: Transaction) {
        //LOGGER.info("********: guid - " + transaction.guid)
        val resultMessage = ResultMessage()
        var resultString:String = ""
        val transaction = Transaction()

        try {
            transactionService?.insertTransaction(transaction)
            resultMessage.message = "Successfully processed add message."
            resultMessage.resultCode = 0
            resultMessage.guid = transaction.guid!!
            resultMessage.setDate(ZonedDateTime.now())

            resultString = mapper.writeValueAsString(resultMessage)

        } catch (jpe: JsonProcessingException) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + jpe + " Exception message:" + jpe.message
            resultMessage.resultCode = 200
        } catch (e: Exception) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + e + " Exception message:" + e.message
            resultMessage.resultCode = 201
        }
    //return resultString
    }

    /*
    //http://localhost:8080/transactions/insertTransaction?accountNameOwner=brian_chase&acccountType=credit&transactionDate=0&description=test&category=test&amount=0.01&cleared=0&notes=empty
    @GetMapping(value = "/insertTransaction")
    fun insertTransaction(@RequestParam accountNameOwner: String, @RequestParam accountType: String, @RequestParam transactionDate: String,
                       @RequestParam description: String, @RequestParam category: String, @RequestParam amount: String,
                       @RequestParam cleared: String, @RequestParam notes: String): String {
        val resultMessage = ResultMessage()
        var resultString:String = ""
        val transaction = Transaction()

        try {
            transaction.guid = UUID.randomUUID().toString()
            //accountNameOwner, required
            transaction.accountNameOwner = accountNameOwner
            transaction.accountType = accountType
            //transactionDate, if empty then now
            val date = java.sql.Date(Calendar.getInstance().time.time)
            //transaction.setTransactionDate(transactionDate);
            transaction.transactionDate = date
            //description, required
            transaction.description = description
            //category, optional
            transaction.category = category
            //amount, required
            transaction.amount = java.lang.Double.parseDouble(amount)
            //cleared, false unless set to true
            transaction.cleared = 0
            transaction.reoccurring = false
            //notes, optional
            transaction.notes = notes
            //TODO: need to fix the save
            //transactionService!!.save(transaction)
            transactionService!!.insertTransaction(transaction)

            resultMessage.message = "Successfully processed add message."
            resultMessage.resultCode = 0
            resultMessage.guid = transaction.guid!!
            resultMessage.setDate(ZonedDateTime.now())

            resultString = mapper.writeValueAsString(resultMessage)
        } catch (jpe: JsonProcessingException) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + jpe + " Exception message:" + jpe.message
            resultMessage.resultCode = 200
        } catch (e: Exception) {
            resultMessage.message = "Failure to processed add message: " + "Exception: " + e + " Exception message:" + e.message
            resultMessage.resultCode = 201
        }

        return resultString
    }
*/

    //http://localhost:8080/transactions/deleteTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @GetMapping(value = "/deleteTransaction/{guid}")
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
