package finance.controllers

import finance.services.TransactionService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import sun.security.x509.OIDMap.addAttribute
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
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

    //localhost:8080/getTransactions
    @RequestMapping(value = "/getTransactions")
    //fun getTransactions(@RequestParam("first") first: Int, @RequestParam("last") last: Int, model: Model): String {
    fun getTransactions(model: Model): String {
        //val transactions = transactionService.getItems(Integer.valueOf(first), Integer.valueOf(last))
        val transactions = transactionService!!.findByAccountNameOwnerIgnoreCaseOrderByTransactionDate("chase_brian")
        model.addAttribute("transactions", transactions)
        return "transaction"
    }

    @RequestMapping("/transaction")
    //@RequestMapping("/transaction")
    fun transaction(): String {
        //var model: Model
        return "transaction"
    }

}