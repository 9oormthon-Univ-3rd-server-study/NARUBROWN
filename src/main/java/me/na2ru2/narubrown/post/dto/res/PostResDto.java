package me.na2ru2.narubrown.post.dto.res;

import lombok.Builder;

@Builder
public record PostResDto(
        Long id,
        String title,
        String contents,
        String author_email
) {
}
