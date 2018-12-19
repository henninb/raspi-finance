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
import java.io.Serializable
import java.util.HashSet

@Entity(name = "TransactionEntity")
@Table(name = "t_transaction")
@JsonDeserialize(using = TransactionDeserializer::class)
@JsonSerialize(using = TransactionSerializer::class)
open class Transaction {
//class Transaction : Serializable {

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
    open var guid: String = ""
    open var accountId: Long = 0
    @Size(min = 5, max = 6)
    open var accountType: String? = null
    @NotNull
    @NotBlank(message = "accountNameOwner cannnot be empty.")
    @Size(min = 1, max = 40)
    open var accountNameOwner: String? = null
    open var transactionDate: Date = Date(0)
    @NotNull
    @NotBlank(message = "description cannnot be empty.")
    @Size(min = 1, max = 75)
    open var description: String? = null
    @Size(max = 50)
    open var category: String? = null
    @NotNull
    open var amount: Double = 0.0
    @NotNull
    @Column(name = "cleared")
    open var cleared: Int = 0
    open var reoccurring: Boolean = true;
    @Size(max = 100)
    open var notes: String? = null
    open var dateUpdated: Timestamp = Timestamp(0)
    open var dateAdded: Timestamp = Timestamp(0)
    @Size(max = 70)
    open var sha256: String? = null

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "accountId", nullable = true, insertable = false, updatable = false)
    @JsonIgnore
    open var account: Account? = null

    //https://stackoverflow.com/questions/51868093/kotlin-data-class-as-jpa-hibernate-embeddable-with-many-to-many-relationship
    //https://www.baeldung.com/hibernate-many-to-many
    //@ManyToMany(cascade = [ CascadeType.PERSIST, CascadeType.MERGE ])
    @ManyToMany(cascade = [ CascadeType.ALL ])
    @JoinTable(name = "t_transaction_categories",
            joinColumns = [JoinColumn(name = "transactionId")],
            inverseJoinColumns = [JoinColumn(name = "categoryId")])
    @JsonIgnore
    open var categries = mutableListOf<Category>()

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}
