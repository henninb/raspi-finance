package finance.repositories;

import finance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT  * FROM t_account WHERE account_type='credit' AND active_status = 'Y' AND account_Name_Owner != 'grand.total_dummy' AND account_Name_Owner !='credit.total_dummy' AND account_Name_Owner != 'debit.total_dummy' ORDER BY account_Name_Owner", nativeQuery = true)
    List<Account> findActiveAccounts();
}
