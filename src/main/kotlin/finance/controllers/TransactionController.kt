package finance.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import finance.models.Transaction
import finance.pojos.Totals
import finance.services.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

//@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@CrossOrigin
//Thymeleaf - RestController is for JSON; Controller is for HTML
@RestController
//@RequestMapping("/transaction")
class TransactionController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var transactionService: TransactionService

    //http://localhost:8080/findall
    //http://localhost:8080/findall?pageNumber=0&pageSize=10
    @GetMapping(path = arrayOf("/findall"))
    fun findAllTransactions(@RequestParam("pageNumber") pageNumber: Optional<Int>, @RequestParam("pageSize") pageSize: Optional<Int>, pageable: Pageable): ResponseEntity<Page<Transaction>> {
        var page = 0
        var size = 10
        if(pageNumber.isPresent ) {
            page = pageNumber.get()
        }

        if( pageSize.isPresent ) {
            size = pageSize.get()
        }
        var pageable1: Pageable = PageRequest.of(page, size)
        var transactions: Page<Transaction> = transactionService.findAllTransactions(pageable1)
        if( transactions.totalElements < size ) {
            pageable1 = PageRequest.of(0, transactions.totalElements.toInt())
            transactions = transactionService.findAllTransactions(pageable1)
        }
        if(transactions.isEmpty) {
            ResponseEntity.notFound().build<List<Transaction>>()
        }
        return ResponseEntity.ok(transactions)
    }
    //http://localhost:8080/get_page_by_account_name_owner/amex
    //http://localhost:8080/get_page_by_account_name_owner/amex_brian?pageNumber=0&pageSize=5
    @GetMapping(path = arrayOf("/get_page_by_account_name_owner/{accountNameOwner}"))
    fun findByAccountNameOwnerPage(@PathVariable accountNameOwner: String, @RequestParam pageNumber: Optional<Int>, @RequestParam pageSize: Optional<Int>, pageable: Pageable): ResponseEntity<Page<Transaction>> {
        var page = 0
        var size = 5
        if(pageNumber.isPresent ) {
            page = pageNumber.get()
        }

        if( pageSize.isPresent ) {
            size = pageSize.get()
        }
        val pageable1: Pageable = PageRequest.of(page, size)
        val transactions: Page<Transaction> = transactionService.findTransactionsByAccountNameOwnerPageable(pageable1, accountNameOwner)
        if( transactions.isEmpty) {
            ResponseEntity.notFound().build<List<Transaction>>()
        }

        return ResponseEntity.ok(transactions)
    }

    //http://localhost:8080/get_by_account_name_owner/amazon.gift_brian
    @GetMapping(path = arrayOf("/get_by_account_name_owner/{accountNameOwner}"))
    fun selectByAccountNameOwner(@PathVariable accountNameOwner: String): ResponseEntity<List<Transaction>> {
        val transactions: List<Transaction> = transactionService.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner)
        if( transactions.isEmpty() ) {
            ResponseEntity.notFound().build<List<Transaction>>()
        }
        return ResponseEntity.ok(transactions)
    }

    //http://localhost:8080/get_totals_cleared/chase_brian
    @GetMapping(path = arrayOf("/get_totals_cleared/{accountNameOwner}"))
    fun selectTotalsCleared(@PathVariable accountNameOwner: String): ResponseEntity<String> {
        val totals: Totals = transactionService.getTotalsByAccountNameOwner(accountNameOwner)

        return ResponseEntity.ok(mapper.writeValueAsString(totals))
    }

    //http://localhost:8080/select/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @GetMapping(path = arrayOf("/select/{guid}"))
    fun findtTransaction(@PathVariable guid: String): ResponseEntity<Transaction> {
        val transactionOption: Optional<Transaction> = transactionService.findByGuid(guid)
        if( transactionOption.isPresent ) {
            val transaction: Transaction = transactionOption.get()
            return ResponseEntity.ok(transaction)
        }
        return ResponseEntity.notFound().build()  //404
    }

    //curl --header "Content-Type: application/json-patch+json" --request PATCH --data '{"guid":"a064b942-1e78-4913-adb3-b992fc1b4dd3","sha256":"","accountType":"credit","accountNameOwner":"discover_brian","description":"Last Updated","category":"","notes":"","cleared":0,"reoccurring":false,"amount":"0.00","transactionDate":1512730594,"dateUpdated":1487332021,"dateAdded":1487332021}' http://localhost:8080/update/a064b942-1e78-4913-adb3-b992fc1b4dd3
    //http://localhost:8080/update/340c315d-39ad-4a02-a294-84a74c1c7ddc
    @PatchMapping(path = arrayOf("/update/{guid}"), consumes = arrayOf("application/json-patch+json"), produces = arrayOf("application/json"))
    fun updateTransaction(@RequestBody transaction: Map<String, String>): ResponseEntity<String> {
        val toBePatchedTransaction = mapper.convertValue(transaction, Transaction::class.java)
        val updateStatus: Boolean = transactionService.patchTransaction(toBePatchedTransaction)
        if( updateStatus == true) {
            return ResponseEntity.ok("transaction updated")
        }
        return ResponseEntity.notFound().build()
    }

    //http://localhost:8080/insert
    @PostMapping(path = arrayOf("/insert"), consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun insertTransaction(@RequestBody transaction: Transaction) : ResponseEntity<String> {
        if (transactionService.insertTransaction(transaction) ) {
            return ResponseEntity.ok("transaction inserted")
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    //curl --header "Content-Type: application/json" --request DELETE http://localhost:8080/delete/38739c5b-e2c6-41cc-82c2-d41f39a33f9a
    //http://localhost:8080/delete/38739c5b-e2c6-41cc-82c2-d41f39a33f9a
    @DeleteMapping(path = arrayOf("/delete/{guid}"))
    fun deleteTransaction(@PathVariable guid: String): ResponseEntity<String> {
        val transactionOption: Optional<Transaction> = transactionService.findByGuid(guid)
        if( transactionOption.isPresent ) {
            if( transactionService.deleteByGuid(guid) ) {
                return ResponseEntity.ok("resource deleted")
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.notFound().build() //404
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}
