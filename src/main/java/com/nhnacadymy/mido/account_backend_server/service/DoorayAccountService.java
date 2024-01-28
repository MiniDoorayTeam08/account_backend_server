package com.nhnacadymy.mido.account_backend_server.service;

import com.nhnacadymy.mido.account_backend_server.domain.AccountDto;
import com.nhnacadymy.mido.account_backend_server.domain.AccountRegisterRequest;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import java.util.List;

public interface DoorayAccountService {

    String deleteAccount(String id);

    String createAccount(AccountRegisterRequest account);

    DoorayAccount login(String id, String pwd);

    String setAccountDormantStatus(String id, String status);

    DoorayAccount getAccount(String accountId);

    List<AccountDto> getAccountList(String accountStatus);
}
