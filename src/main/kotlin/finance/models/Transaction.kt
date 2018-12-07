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
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import org.hibernate.FetchMode.LAZY
import java.util.ArrayList



@Entity(name = "TransactionEntity")
@Table(name = "t_transaction")

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
    @Column(unique=true)
    @NotNull
    @Size(min = 36, max = 36)
    var guid: String = ""
    var accountId: Long = 0
    @Size(min = 5, max = 6)
    var accountType: String? = null
    @NotNull
    @NotBlank(message = "accountNameOwner cannnot be empty.")
    @Size(min = 1, max = 40)
    var accountNameOwner: String? = null
    var transactionDate: Date = Date(0)
    @NotNull
    @NotBlank(message = "description cannnot be empty.")
    @Size(min = 1, max = 75)
    var description: String? = null
    @Size(max = 50)
    var category: String? = null
    @NotNull
    var amount: Double = 0.0
    @NotNull
    @Column(name = "cleared")
    var cleared: Int = 0
    var reoccurring: Boolean = true;
    @Size(max = 100)
    var notes: String? = null
    var dateUpdated: Timestamp = Timestamp(0)
    var dateAdded: Timestamp = Timestamp(0)
    @Size(max = 70)
    var sha256: String? = null

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "accountId", nullable = true, insertable = false, updatable = false)
    @JsonIgnore
    var account: Account? = null

    //https://stackoverflow.com/questions/51868093/kotlin-data-class-as-jpa-hibernate-embeddable-with-many-to-many-relationship

//    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
//    @JoinTable(name = "transaction_category",
//            joinColumns = [JoinColumn(name = "transactionId", referencedColumnName = "transactionId")],
//            inverseJoinColumns = [JoinColumn(name = "categoryId", referencedColumnName = "categoryId")])
//    @JsonIgnore
//    val transactionCategories = mutableListOf<TransactionCategories>()

    //https://www.baeldung.com/hibernate-many-to-many
    @ManyToMany(cascade = [ CascadeType.PERSIST, CascadeType.MERGE ])
    @JoinTable(name = "t_transaction_categories",
            joinColumns = [JoinColumn(name = "transactionId")],
            inverseJoinColumns = [JoinColumn(name = "categoryId")])
    @JsonIgnore
    //var categories = mutableListOf<Category>()
    val transactionCategories = mutableListOf<TransactionCategories>()

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}
