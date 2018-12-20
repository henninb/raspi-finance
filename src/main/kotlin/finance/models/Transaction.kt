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
    internal var guid: String = ""
    internal var accountId: Long = 0
    @Size(min = 5, max = 6)
    internal var accountType: String? = null
    @NotNull
    @NotBlank(message = "accountNameOwner cannnot be empty.")
    @Size(min = 1, max = 40)
    internal var accountNameOwner: String? = null
    internal var transactionDate: Date = Date(0)
    @NotNull
    @NotBlank(message = "description cannnot be empty.")
    @Size(min = 1, max = 75)
    internal var description: String? = null
    @Size(max = 50)
    internal var category: String? = null
    @NotNull
    internal var amount: Double = 0.0
    @NotNull
    @Column(name = "cleared")
    internal var cleared: Int = 0
    internal var reoccurring: Boolean = true;
    @Size(max = 100)
    internal var notes: String? = null
    internal var dateUpdated: Timestamp = Timestamp(0)
    internal var dateAdded: Timestamp = Timestamp(0)
    @Size(max = 70)
    internal var sha256: String? = null

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "accountId", nullable = true, insertable = false, updatable = false)
    @JsonIgnore
    internal var account: Account? = null

    //https://stackoverflow.com/questions/51868093/kotlin-data-class-as-jpa-hibernate-embeddable-with-many-to-many-relationship
    //https://www.baeldung.com/hibernate-many-to-many
    //@ManyToMany(cascade = [ CascadeType.PERSIST, CascadeType.MERGE ])
    @ManyToMany(cascade = [ CascadeType.ALL ])
    @JoinTable(name = "t_transaction_categories",
            joinColumns = [JoinColumn(name = "transactionId")],
            inverseJoinColumns = [JoinColumn(name = "categoryId")])
    @JsonIgnore
    internal var categries = mutableListOf<Category>()

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}
