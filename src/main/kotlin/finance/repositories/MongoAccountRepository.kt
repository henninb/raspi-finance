package finance.repositories

import finance.models.Account
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface MongoAccountRepository<T : Account> : MongoRepository<T, Long> {
    fun findByAccountNameOwner(accountNameOwner: String): Optional<Account>
    fun findByActiveStatusOrderByAccountNameOwner(activeStatus: String): List<Account>
}
