package me.na2ru2.narubrown.post.dto.res;

import lombok.Builder;
import me.na2ru2.narubrown.post.domain.Post;

@Builder
public record PostResDto(
        Long id,
        String title,
        String contents,
        String author_email
) {
    public static PostResDto from(Post post) {
        return PostResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .author_email(post.getUser().getEmail())
                .build();
    }
}
