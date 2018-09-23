package finance.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import com.google.common.hash.Hashing.sha256
import java.sql.Date


@Entity(name = "TransactionEntity")
@Table(name = "t_transaction")
class Transaction {

    constructor()

    constructor(guid: String, accountType: String, accountNameOwner: String, transactionDate: Date, description: String, category: String, amount: Double, cleared: Int, notes: String, dateUpdated: Date, dateAdded: Date, sha256: String) {
        this.transactionId = transactionId
        this.guid = guid
        this.accountType = accountType
        this.accountNameOwner = accountNameOwner
        this.transactionDate = transactionDate
        this.description = description
        this.category = category
        this.amount = amount
        this.cleared = cleared
        this.notes = notes
        this.dateUpdated = dateUpdated
        this.dateAdded = dateAdded
        this.sha256 = sha256
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var transactionId: Int = 0
    var guid: String? = null
    var accountId: Int? = null
    var accountType: String? = null
    var accountNameOwner: String? = null
    var transactionDate: java.sql.Date? = null
    var description: String? = null
    var category: String? = null
    var amount: Double = 0.toDouble()
    @Column(name = "cleared")
    var cleared: Int = 0
    var notes: String? = null
    var dateUpdated: java.sql.Date? = null
    var dateAdded: java.sql.Date? = null
    var sha256: String? = null

    @JsonIgnore
    private val extra: String? = null
}



