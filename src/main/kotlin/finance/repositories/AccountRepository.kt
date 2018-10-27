package finance.repositories

import finance.models.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository<T : Account> : JpaRepository<T, Long> {
    fun findByAccountId(transactionId: Long?): Account
    fun findByAccountNameOwner(accountNameOwner: String): Account
    fun findByActiveStatusOrderByAccountNameOwner(activeStatus: String): List<Account>
}
