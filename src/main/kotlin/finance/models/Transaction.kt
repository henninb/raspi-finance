package finance.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.persistence.*
import java.sql.Timestamp
import finance.utils.TransactionDeserializer
import finance.utils.TransactionSerializer
import java.sql.Date
import javax.validation.constraints.NotNull


@Entity(name = "TransactionEntity")
@Table(name = "t_transaction")
//@Table(
//        name = "t_transaction",
//        uniqueConstraints = {
//            @UniqueConstraint(columnNames = "mask"),
//            @UniqueConstraint(columnNames = "group")
//        }
//)
//ALTER TABLE t_transaction ADD CONSTRAINT transaction_constraint UNIQUE (account_name_owner, transaction_date, description, category, amount, notes);
@JsonDeserialize(using = TransactionDeserializer::class)
@JsonSerialize(using = TransactionSerializer::class)
class Transaction {

    constructor()

    constructor(guid: String, accountType: String, accountNameOwner: String, transactionDate: Date, description: String, category: String, amount: Double, cleared: Int, reoccurring: Boolean, notes: String, dateUpdated: Timestamp, dateAdded: Timestamp, sha256: String) {
        this.guid = guid
        this.accountType = accountType
        this.accountNameOwner = accountNameOwner
        this.transactionDate = transactionDate
        this.description = description
        this.category = category
        this.amount = amount
        this.cleared = cleared
        this.reoccurring = reoccurring
        this.notes = notes
        this.dateUpdated = dateUpdated
        this.dateAdded = dateAdded
        this.sha256 = sha256
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var transactionId: Long = 0L
    var guid: String? = null
    var accountId: Long = 0
    var accountType: String? = null
    //@Column(nullable = false)
    @NotNull
    var accountNameOwner: String? = null
    var transactionDate: Date = Date(0)
    @NotNull
    var description: String? = null
    var category: String? = null
    @NotNull
    var amount: Double = 0.0
    @NotNull
    @Column(name = "cleared")
    var cleared: Int = 0
    var reoccurring: Boolean = true;
    var notes: String? = null
    var dateUpdated: Timestamp = Timestamp(0)
    var dateAdded: Timestamp = Timestamp(0)
    var sha256: String? = null

//    override fun toString(): String {
//        return mapper.writeValueAsString(this)
//    }

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}
