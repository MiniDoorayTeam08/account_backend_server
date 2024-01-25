package com.nhnacadymy.mido.account_backend_server.service;

import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;

public interface DoorayAccountService {

    String deleteAccount(String id);
    String createAccount(DoorayAccount account);
    DoorayAccount login(String id, String pwd);
    String setAccountDormantStatus(String id, String status);

    DoorayAccount getAccount(String accountId);
}
