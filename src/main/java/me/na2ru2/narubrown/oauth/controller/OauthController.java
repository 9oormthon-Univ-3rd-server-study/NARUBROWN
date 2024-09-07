package me.na2ru2.narubrown.oauth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.na2ru2.narubrown.oauth.dto.GoogleUserInfoResDto;
import me.na2ru2.narubrown.oauth.dto.KakaoUserInfoResDto;
import me.na2ru2.narubrown.oauth.service.OauthService;
import me.na2ru2.narubrown.user.dto.res.TokenResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final WebClient webClient;
    private final OauthService oauthService;

    @Value("${spring.oauth.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.oauth.google.client_id}")
    private String googleClientId;

    @Value("${spring.oauth.google.client_password}")
    private String googleClientPassword;

    @GetMapping("/kakao")
    public TokenResDto kakaoLogin(@RequestParam String code) {
        String requestUri = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", "http://localhost:8080/oauth/kakao")
                .queryParam("code", code)
                .toUriString();

        TokenResDto tokenResDto = webClient.post()
                .uri(requestUri)
                .retrieve()
                .bodyToMono(TokenResDto.class)
                .block();
        log.info(tokenResDto.access_token());

        KakaoUserInfoResDto response = webClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + tokenResDto.access_token())
                .retrieve()
                .bodyToMono(KakaoUserInfoResDto.class)
                .block();

        return oauthService.register(response.getKakao_account().getEmail());
    }

    @GetMapping("/google")
    public TokenResDto googleLogin(@RequestParam String code) {

        String requestUri = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/token")
                .queryParam("code", code)
                .queryParam("client_id", googleClientId)
                .queryParam("client_secret", googleClientPassword)
                .queryParam("redirect_uri", "http://localhost:8080/oauth/google")
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        TokenResDto tokenResDto = webClient.post()
                .uri(requestUri)
                .retrieve()
                .bodyToMono(TokenResDto.class)
                .block();
        log.info(tokenResDto.access_token());
        GoogleUserInfoResDto response = webClient.post()
                .uri("https://oauth2.googleapis.com/tokeninfo?access_token=" + tokenResDto.access_token())
                .retrieve()
                .bodyToMono(GoogleUserInfoResDto.class)
                .block();
        log.info(response.email());
        return oauthService.register(response.email());
    }
}
