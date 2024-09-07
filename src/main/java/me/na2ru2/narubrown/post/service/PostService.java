package me.na2ru2.narubrown.post.service;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.post.domain.Post;
import me.na2ru2.narubrown.post.dto.req.PostReqDto;
import me.na2ru2.narubrown.post.dto.res.PostResDto;
import me.na2ru2.narubrown.post.repository.PostRepository;
import me.na2ru2.narubrown.user.domain.User;
import me.na2ru2.narubrown.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResDto findPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post를 찾을 수 없습니다."));
        return PostResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .author_email(post.getUser().getEmail())
                .build();
    }

    public void save(PostReqDto postReqDto, Long user_id) {
        User foundUser = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User를 찾을 수 없습니다."));
        Post newPost = Post.builder()
                .title(postReqDto.title())
                .contents(postReqDto.contents())
                .user(foundUser)
                .build();
        postRepository.save(newPost);
    }

}
