package Models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_friends")
public class UserFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_friends_id_gen")
    @SequenceGenerator(name = "user_friends_id_gen", sequenceName = "user_friends_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login", nullable = false, referencedColumnName = "login")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_login", nullable = false, referencedColumnName = "login")
    private User friend;

    public UserFriend() {}

    public UserFriend(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
