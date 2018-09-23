package finance.services;

import finance.model.Account;
import finance.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AccountRepository accountRepository;

    public List<Account> findActiveAccounts() {
        return accountRepository.findActiveAccounts();
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
