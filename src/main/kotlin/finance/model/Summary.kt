package finance.model

import javax.persistence.*

@Entity(name = "SummaryEntity")
@Table(name = "t_summary")
class Summary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var summaryId: Int = 0

    var accountNameOwner: String? = null
    var totals: Double = 0.toDouble()
    var totalsBalanced: Double = 0.toDouble()
    var dateUpdated: String? = null
    var dateAdded: String? = null
}