package com.nhnacadymy.mido.account_backend_server.service;

import com.nhnacadymy.mido.account_backend_server.domain.AccountDto;
import com.nhnacadymy.mido.account_backend_server.domain.AccountRegisterRequest;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import com.nhnacadymy.mido.account_backend_server.exception.AccountAlreadyException;
import com.nhnacadymy.mido.account_backend_server.exception.AccountNotExistException;
import com.nhnacadymy.mido.account_backend_server.repository.DoorayAccountRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoorayAccountServiceImpl implements DoorayAccountService {
    private final DoorayAccountRepository doorayAccountRepository;

    public DoorayAccountServiceImpl(DoorayAccountRepository doorayAccountRepository) {
        this.doorayAccountRepository = doorayAccountRepository;
    }

    @Override
    public String deleteAccount(String id) {
        doorayAccountRepository.deleteById(id);
        return id + "is deleted";
    }

    @Override
    @Transactional
    public String createAccount(AccountRegisterRequest account) {
        boolean isExist = doorayAccountRepository.existsById(account.getId());
        if (isExist) throw new AccountAlreadyException();
        doorayAccountRepository.save(AccountRegisterRequest.createAccount(account));
        return "create : " + account.getId();
    }

    @Override
    public DoorayAccount login(String id, String pwd) {
        return doorayAccountRepository.findByIdAndPassword(id, pwd).orElseThrow(AccountNotExistException::new);
    }


    @Override
    public DoorayAccount getAccount(String accountId) {
        return doorayAccountRepository.findById(accountId).orElseThrow(AccountNotExistException::new);
    }

    @Override
    public List<AccountDto> getAccountList(String accountStatus) {
        return doorayAccountRepository.findAllByAccountStatus("가입");
    }

    @Override
    public String setAccountDormantStatus(String id, String status) {
        DoorayAccount account = doorayAccountRepository.findById(id)
                .orElseThrow(AccountNotExistException::new);

        account.setAccountStatus(status);
        doorayAccountRepository.save(account);

        return "Account (" + id + ") : now set status to " + status;
    }
}
