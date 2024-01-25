package com.nhnacadymy.mido.account_backend_server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
public class DoorayAccount {
    @Id
    private String id;

    private String password;
    private String email;
    private String accountStatus;
}