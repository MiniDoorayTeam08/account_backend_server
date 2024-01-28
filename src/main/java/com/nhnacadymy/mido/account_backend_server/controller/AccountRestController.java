package com.nhnacadymy.mido.account_backend_server.controller;

import com.nhnacadymy.mido.account_backend_server.domain.AccountDto;
import com.nhnacadymy.mido.account_backend_server.domain.AccountRegisterRequest;
import com.nhnacadymy.mido.account_backend_server.domain.LoginRequest;
import com.nhnacadymy.mido.account_backend_server.domain.SetAccountRequest;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import com.nhnacadymy.mido.account_backend_server.service.DoorayAccountService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountRestController {

    private final DoorayAccountService doorayAccountService;

    public AccountRestController(DoorayAccountService accountService) {
        this.doorayAccountService = accountService;
    }

    @GetMapping("/{accountId}")
    public DoorayAccount getAccount(@PathVariable String accountId) {
        return doorayAccountService.getAccount(accountId);
    }

    @GetMapping
    public List<AccountDto> getAccounts() {
        return doorayAccountService.getAccountList("가입");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestBody AccountRegisterRequest doorayAccount) {
        return doorayAccountService.createAccount(doorayAccount);
    }

    @PostMapping("/login")
    public DoorayAccount login(@RequestBody LoginRequest request) {
        return doorayAccountService.login(request.getId(), request.getPassword());
    }

    @PutMapping("/status")
    public String setAccountStatus(@RequestBody SetAccountRequest request) {
        return doorayAccountService.setAccountDormantStatus(request.getId(), request.getStatus());
    }
}
