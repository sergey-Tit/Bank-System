package com.Titarenko.Models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bank_account_creation_events")
public class BankAccountCreationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "owner_login", nullable = false, length = Integer.MAX_VALUE)
    private String ownerLogin;

    @Column(name = "date", nullable = false)
    private Instant date;

    public BankAccountCreationEvent() {
    }

    public BankAccountCreationEvent(String ownerLogin, Instant date) {
        this.ownerLogin = ownerLogin;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
