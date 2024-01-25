package com.nhnacadymy.mido.account_backend_server.service;

import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import com.nhnacadymy.mido.account_backend_server.exception.AccountAlreadyException;
import com.nhnacadymy.mido.account_backend_server.exception.AccountNotExistException;
import com.nhnacadymy.mido.account_backend_server.repository.DoorayAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class DoorayAccountServiceImpl implements DoorayAccountService{
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
    public String createAccount(DoorayAccount account) {
        boolean isExist = doorayAccountRepository.existsById(account.getId());
        if(isExist) throw new AccountAlreadyException();
        doorayAccountRepository.save(account);
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
    public String setAccountDormantStatus(String id, String status ) {
        DoorayAccount account = doorayAccountRepository.findById(id)
                .orElseThrow(AccountNotExistException::new);

        account.setAccountStatus(status);
        doorayAccountRepository.save(account);

        return "Account (" + id + ") : now set status to " + status;
    }
}