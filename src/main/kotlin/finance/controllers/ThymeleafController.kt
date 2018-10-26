package finance.controllers

import finance.services.TransactionService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import sun.security.x509.OIDMap.addAttribute
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam



//@CrossOrigin(origins = arrayOf("http://localhost:3000"))
//Thymeleaf and RestController do not work
//@RestController
@Controller
class ThymeleafController {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var transactionService: TransactionService? = null

    //localhost:8080/
    @RequestMapping("/")
    fun index(): String {
        //var model: Model
        return "index"
    }

    //http://localhost:8080/getTransactions/amex_brian
    @GetMapping(path = arrayOf("/getTransactions/{accountNameOwner}"))
    fun getTransactions(@PathVariable accountNameOwner: String, model: Model): String {
        val transactions = transactionService!!.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate(accountNameOwner)
        model.addAttribute("transactions", transactions)
        return "transaction"
    }
}