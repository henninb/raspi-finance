package finance.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
open class TransactionJdbcTemplateRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate;

    //@Autowired constructor(dataSource: DataSource) {
    //}

    //private val jdbcTemplate: JdbcTemplate

    //init {
    //    this.jdbcTemplate = JdbcTemplate(dataSource)
    //}

    private fun delete(from: String) = jdbcTemplate.update("DELETE FROM $from")

    open fun deleteMyTable() = delete("t_transaction")

    open fun createSummaryTest() = jdbcTemplate.execute("CREATE TABLE t_category_test(category_id INTEGER NOT NULL, category VARCHAR(50))")
}
