package com.nhnacadymy.mido.account_backend_server.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacadymy.mido.account_backend_server.domain.AccountDto;
import com.nhnacadymy.mido.account_backend_server.domain.AccountRegisterRequest;
import com.nhnacadymy.mido.account_backend_server.domain.LoginRequest;
import com.nhnacadymy.mido.account_backend_server.domain.SetAccountRequest;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import com.nhnacadymy.mido.account_backend_server.exception.AccountAlreadyException;
import com.nhnacadymy.mido.account_backend_server.exception.AccountNotExistException;
import com.nhnacadymy.mido.account_backend_server.service.DoorayAccountService;
import java.util.Arrays;
import java.util.List;
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
        given(doorayAccountService.createAccount(any())).willThrow(new AccountAlreadyException());

        mvc.perform(post("/accounts/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(existAccount)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void login() throws Exception {
        String id = "testId";
        String pwd = "password";
        LoginRequest loginRequest = new LoginRequest(id, pwd);
        DoorayAccount mockAccount = new DoorayAccount(id, pwd, "email", "status");
        given(doorayAccountService.login(id, pwd)).willReturn(mockAccount);

        mvc.perform(post("/accounts/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    // 로그인 실패 테스트 (계정이 존재하지 않을 때)
    @Test
    void loginNotFound() throws Exception {
        String id = "nonExistId";
        String pwd = "password";
        LoginRequest loginRequest = new LoginRequest(id, pwd);
        given(doorayAccountService.login(id, pwd)).willThrow(new AccountNotExistException());

        mvc.perform(post("/accounts/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());
    }

    // 계정 상태 변경 테스트
    @Test
    void setAccountStatus() throws Exception {
        String id = "testId";
        String status = "dormant";
        SetAccountRequest request = new SetAccountRequest(id, status);
        given(doorayAccountService.setAccountDormantStatus(id, status)).willReturn("Account (" + id + ") : now set status to " + status);

        mvc.perform(put("/accounts/status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(id)));
    }

    // 계정 상태 변경 실패 테스트 (계정이 존재하지 않을 때)
    @Test
    void setAccountStatusNotExist() throws Exception {
        String id = "nonExistId";
        String status = "dormant";
        SetAccountRequest request = new SetAccountRequest(id, status);
        given(doorayAccountService.setAccountDormantStatus(id, status)).willThrow(new AccountNotExistException());

        mvc.perform(put("/accounts/status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // 계정 목록 조회 테스트
    @Test
    void getAccounts() throws Exception {
        List<AccountDto> accountDtos = Arrays.asList(new AccountDtoImpl("testId1"), new AccountDtoImpl("testId2"));
        given(doorayAccountService.getAccountList("가입")).willReturn(accountDtos);

        mvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo("testId1")))
                .andExpect(jsonPath("$[1].id", equalTo("testId2")));
    }
        public static class AccountDtoImpl implements AccountDto {
            private String id;

        public AccountDtoImpl(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}