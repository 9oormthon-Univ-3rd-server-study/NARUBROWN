package me.na2ru2.narubrown.user.service;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.security.jwt.JwtProvider;
import me.na2ru2.narubrown.user.domain.User;
import me.na2ru2.narubrown.user.dto.req.UserReqDto;
import me.na2ru2.narubrown.user.dto.res.TokenResDto;
import me.na2ru2.narubrown.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signUp(UserReqDto userReqDto) {
        User signedUser = User.builder()
                .email(userReqDto.email())
                .username(userReqDto.username())
                .password(passwordEncoder.encode(userReqDto.password()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        User result = userRepository.save(signedUser);
        return result.getId();
    }

    public TokenResDto signIn(UserReqDto userReqDto) {
        User foundUser = userRepository.findByEmail(userReqDto.email())
                .orElseThrow(() -> new RuntimeException("User를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(userReqDto.password(), foundUser.getPassword())) {
            throw new RuntimeException("Password가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(userReqDto.email());
        return TokenResDto.builder()
                .access_token(accessToken)
                .build();
    }


}
