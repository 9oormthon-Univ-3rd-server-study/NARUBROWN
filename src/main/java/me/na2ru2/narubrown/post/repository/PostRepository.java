package me.na2ru2.narubrown.post.repository;

import me.na2ru2.narubrown.post.domain.Post;
import me.na2ru2.narubrown.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser(User user, Pageable pageable);
}
