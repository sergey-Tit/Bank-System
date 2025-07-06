package Models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = Integer.MAX_VALUE, unique = true)
    private String login;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false, length = Integer.MAX_VALUE)
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_colour", nullable = false, length = Integer.MAX_VALUE)
    private HairColour hairColour;

    public User(String login, String name, int age, String gender, HairColour hairColour) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColour = hairColour;
    }

    public User() {}

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

    public HairColour getHairColour() {
        return hairColour;
    }

    public void setHairColour(HairColour hairColour) {
        this.hairColour = hairColour;
    }

    @Override
    public String toString() {
        return "User info:\n" +
                "Login: " + login + "\n" +
                "Name: " + name + "\n" +
                "Age: " + age + "\n" +
                "Gender: " + gender + "\n" +
                "Hair Colour: " + hairColour + "\n";
    }
}
