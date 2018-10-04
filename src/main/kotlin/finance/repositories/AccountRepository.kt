package finance.repositories

import finance.models.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByAccountId(transactionId: Long?): Account
    fun findByAccountNameOwner(accountNameOwner: String): Account
    //fun findByAccountNameOwnerIgnoreCaseOrderByAccountNameOwner(accountNameOwner: String): List<Account>
}
