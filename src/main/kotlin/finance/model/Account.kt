package finance.model

import javax.persistence.*
import java.sql.Date

@Entity
@Table(name = "t_account")
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var accountId: Int = 0
    var accountNameOwner: String? = null
    var accountType: String? = null
    var activeStatus: String? = null
    var moniker: String? = null
    var totals: Double? = null
    var totalsBalanced: Double? = null
    var dateClosed: Date? = null
    var dateUpdated: Date? = null
    var dateAdded: Date? = null
        private set

    fun setDate_added(dateAdded: Date) {
        this.dateAdded = dateAdded
    }
}
