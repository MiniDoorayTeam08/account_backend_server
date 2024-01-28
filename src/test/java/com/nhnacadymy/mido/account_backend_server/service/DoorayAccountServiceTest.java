package com.nhnacadymy.mido.account_backend_server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacadymy.mido.account_backend_server.domain.AccountDto;
import com.nhnacadymy.mido.account_backend_server.domain.AccountRegisterRequest;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import com.nhnacadymy.mido.account_backend_server.exception.AccountAlreadyException;
import com.nhnacadymy.mido.account_backend_server.exception.AccountNotExistException;
import com.nhnacadymy.mido.account_backend_server.repository.DoorayAccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DoorayAccountServiceTest {

    @Mock
    private DoorayAccountRepository doorayAccountRepository;

    @InjectMocks
    private DoorayAccountServiceImpl doorayAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteAccount() {
        String id = "testId";
        doorayAccountService.deleteAccount(id);
        verify(doorayAccountRepository).deleteById(id);
    }

    @Test
    void createAccount() {
        AccountRegisterRequest request = new AccountRegisterRequest();
        request.setId("newId");
        when(doorayAccountRepository.existsById("newId")).thenReturn(false);
        String result = doorayAccountService.createAccount(request);
        verify(doorayAccountRepository).save(any(DoorayAccount.class));
        assertEquals("create : newId", result);
    }

    @Test
    void createAccount_ThrowsException_WhenAccountExists() {
        AccountRegisterRequest request = new AccountRegisterRequest();
        request.setId("existingId");
        when(doorayAccountRepository.existsById("existingId")).thenReturn(true);
        assertThrows(AccountAlreadyException.class, () -> doorayAccountService.createAccount(request));
    }

    @Test
    void login() {
        String id = "user";
        String pwd = "password";
        DoorayAccount account = new DoorayAccount();
        when(doorayAccountRepository.findByIdAndPassword(id, pwd)).thenReturn(Optional.of(account));
        DoorayAccount result = doorayAccountService.login(id, pwd);
        assertNotNull(result);
    }

    @Test
    void login_ThrowsException_WhenAccountNotExists() {
        when(doorayAccountRepository.findByIdAndPassword("user", "password")).thenReturn(Optional.empty());
        assertThrows(AccountNotExistException.class, () -> doorayAccountService.login("user", "password"));
    }
    @Test
    void getAccount() {
        String accountId = "accountId";
        DoorayAccount account = new DoorayAccount();
        when(doorayAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        DoorayAccount result = doorayAccountService.getAccount(accountId);
        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void getAccount_ThrowsException_WhenAccountNotExists() {
        String accountId = "accountId";
        when(doorayAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotExistException.class, () -> doorayAccountService.getAccount(accountId));
    }

    @Test
    void getAccountList() {
        List<AccountDto> expectedAccounts = new ArrayList<>();
        when(doorayAccountRepository.findAllByAccountStatus("가입")).thenReturn(expectedAccounts);

        List<AccountDto> result = doorayAccountService.getAccountList("가입");
        assertNotNull(result);
        assertEquals(expectedAccounts, result);
    }

    @Test
    void setAccountDormantStatus() {
        String id = "accountId";
        String status = "DORMANT";
        DoorayAccount account = new DoorayAccount();
        when(doorayAccountRepository.findById(id)).thenReturn(Optional.of(account));

        String result = doorayAccountService.setAccountDormantStatus(id, status);
        assertNotNull(result);
        assertEquals("Account (" + id + ") : now set status to " + status, result);
        assertEquals(status, account.getAccountStatus());
        verify(doorayAccountRepository).save(account);
    }
}