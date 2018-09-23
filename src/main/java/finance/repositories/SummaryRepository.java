package finance.repositories;

import finance.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Long> {

    // Using SpEL expression
    @Query(value="FROM SummaryEntity WHERE guid = :guid ORDER BY accountNameOwner")
    List<Summary> fetchSummary1(@Param("guid") String guid);

    @Query(value = "SELECT guid FROM t_summary ORDER BY date_added DESC LIMIT 1", nativeQuery = true)
    public String fetchGuid();

    @Query(value = "SELECT * FROM t_summary WHERE guid = :guid ORDER BY account_name_owner", nativeQuery = true)
    List<Summary> fetchSummary(@Param("guid") String guid);

    // Using SpEL expression
    //should only return 1 value
    @Query(value="FROM SummaryEntity WHERE guid = :guid AND accountNameOwner = :accountNameOwner ORDER BY accountNameOwner")
    List<Summary> fetchSummaryByAccountNameOwner(@Param("guid") String guid, @Param("accountNameOwner") String accountNameOwner);
}
