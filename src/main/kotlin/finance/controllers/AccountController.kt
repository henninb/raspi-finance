package finance.controllers


import com.fasterxml.jackson.databind.ObjectMapper
import finance.models.Account
import finance.services.AccountService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

//@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@CrossOrigin
@RestController
//@RequestMapping("/account")
class AccountController {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping(path = arrayOf("/select_accounts"))
    fun selectAllActiveAccounts(): ResponseEntity<List<Account>> {
        val accounts : List<Account> = accountService.findAllAcitveAccounts()
        if( accounts.isEmpty() ) {
            LOGGER.info("no accounts found.")
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(accounts)
    }

    @GetMapping(path = arrayOf("/select_all"))
    fun select_all_accounts(): ResponseEntity<List<Account>> {
        val accounts : List<Account> = accountService.findAllOrderByAccountNameOwner()
        if( accounts.isEmpty() ) {
            LOGGER.info("no accounts found.")
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(accounts)
    }

    @GetMapping(path = arrayOf("/select_account/{accountNameOwner}"))
    fun select_account(@PathVariable accountNameOwner: String): ResponseEntity<Account> {
        val accountOptional: Optional<Account> = accountService.findByAccountNameOwner(accountNameOwner)
        if( accountOptional.isPresent) {
            return ResponseEntity.ok(accountOptional.get())
        }
        return ResponseEntity.notFound().build()
    }

    //http://localhost:8080/insert_account
    @PostMapping(path = arrayOf("/insert_account"), consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun insert_account(@RequestBody account: Account) : ResponseEntity<String> {
        accountService.insertAccount(account)
        return ResponseEntity.ok("account inserted")
    }

    //http://localhost:8080/delete_account/amex_brian
    @DeleteMapping(path = arrayOf("/delete_account/{accountNameOwner}"))
    fun delete_account(@PathVariable accountNameOwner: String): ResponseEntity<String> {
        val accountOptional: Optional<Account> = accountService.findByAccountNameOwner(accountNameOwner)

        if(accountOptional.isPresent ) {
            accountService.deleteByAccountNameOwner(accountNameOwner)
            return ResponseEntity.ok("account deleted")
        }
        return ResponseEntity.notFound().build() //404
    }


    @PatchMapping(path = arrayOf("/update_account/{accountNameOwner}"), consumes = arrayOf("application/json-patch+json"), produces = arrayOf("application/json"))
    fun updateTransaction(@RequestBody account: Map<String, String>): ResponseEntity<String> {
        val toBePatchedTransaction = mapper.convertValue(account, Account::class.java)
        val updateStatus: Boolean = accountService.patchAccount(toBePatchedTransaction)
        if( updateStatus == true ) {
            return ResponseEntity.ok("account updated")
        }
        return ResponseEntity.notFound().build()
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}
