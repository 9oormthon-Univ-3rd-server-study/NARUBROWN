package me.na2ru2.narubrown.post.repository;

import me.na2ru2.narubrown.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
