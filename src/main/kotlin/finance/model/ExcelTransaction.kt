package finance.model

import com.fasterxml.jackson.annotation.JsonProperty

class ExcelTransaction {
    //var accountId: Int = 0
    //var transactionId: Int = 0
    @JsonProperty("cleared") var cleared: String? = null
    var guid: String? = null
    var description: String? = null
    var category: String? = null
    var amount: String? = null
    var notes: String? = null
    var dateAdded: String? = null
    var dateUpdated: String? = null
    var transactionDate: String? = null
    var accountNameOwner: String? = null
    var sha256: String? = null
    var accountType: String? = null
}
