package com.nhnacadymy.mido.account_backend_server.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacadymy.mido.account_backend_server.domain.AccountRegisterRequest;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import com.nhnacadymy.mido.account_backend_server.exception.AccountAlreadyException;
import com.nhnacadymy.mido.account_backend_server.exception.AccountNotExistException;
import com.nhnacadymy.mido.account_backend_server.service.DoorayAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountRestController.class)
class AccountRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DoorayAccountService doorayAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAccount() throws Exception {
        String accountId = "testId";
        DoorayAccount mockAccount = new DoorayAccount(accountId, "password", "email", "status");
        given(doorayAccountService.getAccount(accountId)).willReturn(mockAccount);

        mvc.perform(get("/accounts/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId));
    }

    @Test
    void getAccountNotExist() throws Exception {
        String accountId = "nonExistId";
        given(doorayAccountService.getAccount(accountId)).willThrow(new AccountNotExistException());

        mvc.perform(get("/accounts/{accountId}", accountId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAccount() throws Exception {
        AccountRegisterRequest newAccount = new AccountRegisterRequest("newId", "password", "email");
        given(doorayAccountService.createAccount(newAccount)).willReturn("create : newId");

        mvc.perform(post("/accounts/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccountAlreadyExist() throws Exception {
        AccountRegisterRequest existAccount = new AccountRegisterRequest("existId", "password", "email");
        given(doorayAccountService.createAccount(existAccount)).willThrow(new AccountAlreadyException());

        mvc.perform(post("/accounts/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(existAccount)))
                .andExpect(status().isBadRequest());
    }
}