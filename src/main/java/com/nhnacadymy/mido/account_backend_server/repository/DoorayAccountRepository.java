package com.nhnacadymy.mido.account_backend_server.repository;

import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoorayAccountRepository extends JpaRepository<DoorayAccount, String> {
    Optional<DoorayAccount> findByIdAndPassword(String id, String password);
}