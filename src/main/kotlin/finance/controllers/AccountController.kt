package finance.controllers


import finance.models.Account
import finance.services.AccountService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
        val account: Account = accountService.findByAccountNameOwner(accountNameOwner)
        return ResponseEntity.ok(account)
    }
}
