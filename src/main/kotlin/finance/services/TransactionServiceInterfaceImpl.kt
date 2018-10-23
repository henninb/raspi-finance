package finance.services

//https://github.com/chathurangat/spring-boot-jpa-pagination-example
//https://stackoverflow.com/questions/32434058/how-to-implement-pagination-in-spring-boot-with-hibernate

import finance.models.Transaction
import finance.repositories.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
open class TransactionServiceInterfaceImpl : TransactionServiceInterface {

    @Autowired
    lateinit private var transactionRepository: TransactionRepository<Transaction>

    override fun create(transaction: Transaction): Transaction {
        return transactionRepository.save(transaction)
    }

    override fun findTransactions(pageable: Pageable): Page<Transaction> {
        return transactionRepository.findAll(pageable)
    }
}
