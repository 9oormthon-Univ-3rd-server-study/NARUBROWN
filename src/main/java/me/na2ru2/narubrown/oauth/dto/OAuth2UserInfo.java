package me.na2ru2.narubrown.oauth.dto;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import me.na2ru2.narubrown.user.domain.User;

import java.util.Collections;
import java.util.Map;

@Builder
@Slf4j
public record OAuth2UserInfo(
        Map<String,Object> attributes,
        String nameAttributesKey,
        String name,
        String email,
        String profile
) {
    // attributes -> 응답 묶음...
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(attributes);
        } else if (registrationId.equals("kakao")) {
            return ofKakao(attributes);
        } else {
            throw new RuntimeException("일치하는 registrationId가 없습니다.");
        }
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .nameAttributesKey("email")
                .attributes(attributes)
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .nameAttributesKey("email")
                .attributes(account)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .username(name)
                .email(email)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
