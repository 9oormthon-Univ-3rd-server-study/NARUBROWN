package me.na2ru2.narubrown.oauth.service;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.security.jwt.JwtProvider;
import me.na2ru2.narubrown.user.domain.User;
import me.na2ru2.narubrown.user.dto.res.TokenResDto;
import me.na2ru2.narubrown.user.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public TokenResDto register(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isEmpty()) {
            User newUser = User.builder()
                    .email(email)
                    .username(RandomStringUtils.randomAlphabetic(4))
                    .password(RandomStringUtils.randomAlphabetic(15))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userRepository.save(newUser);
        }
        return TokenResDto.builder()
                .access_token(jwtProvider.createAccessToken(email))
                .build();
    }
}
