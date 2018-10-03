package finance.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import finance.utils.AccountDeserializer
import finance.utils.AccountSerializer
import javax.persistence.*
import java.sql.Timestamp

@Entity(name = "AccountEntity")
@Table(name = "t_account")
@JsonDeserialize(using = AccountDeserializer::class)
@JsonSerialize(using = AccountSerializer::class)
class Account {

    constructor()

    constructor(accountNameOwner: String, accountType: String, activeStatus: String, moniker: String, totals: Double, totalsBalanced: Double, dateClosed: Timestamp, dateUpdated: Timestamp, dateAdded: Timestamp) {
        //this.accountId = accountId
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
    var accountId: Int = 0
    var accountNameOwner: String? = null
    var accountType: String? = null
    var activeStatus: String? = null
    var moniker: String? = null
    var totals: Double = 0.toDouble()
    var totalsBalanced: Double = 0.toDouble()
    var dateClosed: Timestamp? = null
    var dateUpdated: Timestamp? = null
    var dateAdded: Timestamp? = null

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}