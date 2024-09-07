package me.na2ru2.narubrown.user.dto.req;

import lombok.Builder;

@Builder
public record UserReqDto(
        String username,
        String password,
        String email
) {
}
