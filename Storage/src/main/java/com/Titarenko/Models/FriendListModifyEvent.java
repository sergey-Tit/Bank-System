package com.Titarenko.Models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "friend_list_modify_events")
public class FriendListModifyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "operation", nullable = false, length = Integer.MAX_VALUE)
    private String operation;

    @Column(name = "friend_login", nullable = false, length = Integer.MAX_VALUE)
    private String friendLogin;

    @Column(name = "initiator_login", nullable = false, length = Integer.MAX_VALUE)
    private String initiatorLogin;

    @Column(name = "date", nullable = false)
    private Instant date;

    public FriendListModifyEvent() {
    }

    public FriendListModifyEvent(String operation, String friendLogin, String initiatorLogin, Instant date) {
        this.operation = operation;
        this.friendLogin = friendLogin;
        this.initiatorLogin = initiatorLogin;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFriendLogin() {
        return friendLogin;
    }

    public void setFriendLogin(String friendLogin) {
        this.friendLogin = friendLogin;
    }

    public String getInitiatorLogin() {
        return initiatorLogin;
    }

    public void setInitiatorLogin(String initiatorLogin) {
        this.initiatorLogin = initiatorLogin;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant timestamp) {
        this.date = timestamp;
    }
}
