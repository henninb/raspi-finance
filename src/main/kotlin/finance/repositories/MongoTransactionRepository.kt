package finance.repositories

import finance.models.Transaction
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.data.mongodb.repository.MongoRepository


@ConditionalOnExpression("\${project.mongo.feature:false}")
interface MongoTransactionRepository : MongoRepository<Transaction, String> {
//    fun findByDescriptionIgnoreCase(description: String): Transaction
//    fun findByTransactionId(transactionId: Long?): Transaction
}
