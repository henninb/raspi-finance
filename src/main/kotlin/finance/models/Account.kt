package finance.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import finance.utils.AccountDeserializer
import finance.utils.AccountSerializer
import javax.persistence.*
import java.sql.Timestamp
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity(name = "AccountEntity")
@Table(name = "t_account")
@JsonDeserialize(using = AccountDeserializer::class)
@JsonSerialize(using = AccountSerializer::class)
class Account {

    //@JsonIgnore
    //val LOGGER = LoggerFactory.getLogger(this.javaClass)

    constructor()

    constructor(accountNameOwner: String, accountType: String, activeStatus: String, moniker: String, totals: Double, totalsBalanced: Double, dateClosed: Timestamp, dateUpdated: Timestamp, dateAdded: Timestamp) {
        this.accountNameOwner = accountNameOwner
        this.accountType = accountType
        this.activeStatus = activeStatus
        this.moniker = moniker
        this.totals = totals
        this.totalsBalanced = totalsBalanced
        this.dateClosed = dateClosed
        this.dateUpdated = dateUpdated
        this.dateAdded = dateAdded
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var accountId: Long = 0L
    var accountNameOwner: String? = null
    //@Enumerated(EnumType.STRING)
    @Size(min = 5, max = 6)
    var accountType: String? = null
    var activeStatus: String? = null
    var moniker: String? = null
    var totals: Double = 0.0
    var totalsBalanced: Double = 0.0
    var dateClosed: Timestamp = Timestamp(0)
    var dateUpdated: Timestamp = Timestamp(0)
    var dateAdded: Timestamp = Timestamp(0)

    override fun toString(): String {
        System.out.println("this.accountid: " + this.accountId)
        return mapper.writeValueAsString(this)
    }

    //override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}