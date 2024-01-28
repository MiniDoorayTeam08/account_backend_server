package com.nhnacadymy.mido.account_backend_server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacadymy.mido.account_backend_server.domain.AccountDto;
import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class DoorayAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DoorayAccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        DoorayAccount account = new DoorayAccount();
        account.setId("test1");
        account.setPassword("password1");
        account.setAccountStatus("가입");
        entityManager.persist(account);

        DoorayAccount account2 = new DoorayAccount();
        account2.setId("test2");
        account2.setPassword("password2");
        account2.setAccountStatus("휴면");
        entityManager.persist(account2);
    }

    @Test
    void findByIdAndPassword() {
        Optional<DoorayAccount> result = accountRepository.findByIdAndPassword("test1", "password1");
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("test1");
    }

    @Test
    void findAllByAccountStatus() {
        List<AccountDto> results = accountRepository.findAllByAccountStatus("가입");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo("test1");
    }
}