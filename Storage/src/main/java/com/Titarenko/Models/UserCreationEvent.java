package com.Titarenko.Models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users_creation_events")
public class UserCreationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = Integer.MAX_VALUE)
    private String login;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false, length = Integer.MAX_VALUE)
    private String gender;

    @Column(name = "hair_colour", nullable = false, length = Integer.MAX_VALUE)
    private String hairColour;

    @Column(name = "date", nullable = false)
    private Instant date;

    public UserCreationEvent() {}

    public UserCreationEvent(Long id, String login, String name, Integer age, String gender, String hairColour, Instant date) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColour = hairColour;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHairColour() {
        return hairColour;
    }

    public void setHairColour(String hairColour) {
        this.hairColour = hairColour;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Instant getDate() {
        return date;
    }
}
