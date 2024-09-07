package me.na2ru2.narubrown.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.na2ru2.narubrown.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String contents, User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
    }
}
