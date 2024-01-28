package com.nhnacadymy.mido.account_backend_server.domain;

import com.nhnacadymy.mido.account_backend_server.entity.DoorayAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterRequest {
    String id;
    String password;
    String email;

    public static DoorayAccount createAccount(AccountRegisterRequest request) {
        return new DoorayAccount(request.id, request.password, request.email, "가입");
    }
}
