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
//@AccessType("field")
@Table(name = "t_account")
@JsonDeserialize(using = AccountDeserializer::class)
@JsonSerialize(using = AccountSerializer::class)
open class Account {
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
    open var accountNameOwner: String? = null
    //TODO: change to ennum
    //@Enumerated(EnumType.STRING)
    @Size(min = 5, max = 6)
    internal var accountType: String? = null
    internal var activeStatus: String? = null
    internal var moniker: String? = null
    internal var totals: Double = 0.0
    internal var totalsBalanced: Double = 0.0
    internal var dateClosed: Timestamp = Timestamp(0)
    internal var dateUpdated: Timestamp = Timestamp(0)
    internal var dateAdded: Timestamp = Timestamp(0)

    override fun toString(): String {
        return mapper.writeValueAsString(this)
    }

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}