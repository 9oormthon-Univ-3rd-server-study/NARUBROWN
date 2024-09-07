package me.na2ru2.narubrown.user.dto.res;

import lombok.Builder;

@Builder
public record TokenResDto(
        String access_token,
        String refresh_token
) {
}
