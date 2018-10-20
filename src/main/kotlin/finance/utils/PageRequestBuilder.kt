package finance.utils

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.substringAfter
import net.logstash.logback.encoder.org.apache.commons.lang.ObjectUtils.defaultIfNull
import org.springframework.data.domain.PageRequest
import java.util.stream.Collectors
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.defaultIfEmpty
import org.springframework.util.ObjectUtils
import org.springframework.util.StringUtils
import java.util.*
import javax.persistence.criteria.Order


object PageRequestBuilder {
/*
    fun getPageRequest(pageSize: Int?, pageNumber: Int?, sortingCriteria: String): PageRequest {

        val sortingFileds = LinkedHashSet(
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sortingCriteria, ""), ",")))

        val sortingOrders = sortingFileds.stream().map(Function<String, R> { getOrder(it) })
                .collect(Collectors.toList<Any>())

        val sort = if (sortingOrders.isEmpty()) null else Sort(sortingOrders)

        return PageRequest(ObjectUtils.defaultIfNull(pageNumber, 1) - 1, ObjectUtils.defaultIfNull(pageSize, 20),
                sort)
    }

    private fun getOrder(value: String): Order {

        return if (StringUtils.startsWith(value, "-")) {
            Order(Direction.DESC, StringUtils.substringAfter(value, "-"))
        } else if (StringUtils.startsWith(value, "+")) {
            Order(Direction.ASC, StringUtils.substringAfter(value, "+"))
        } else {
            // Sometimes '+' from query param can be replaced as ' '
            Order(Direction.ASC, StringUtils.trim(value))
        }
    }
}// Do nothing
*/
}