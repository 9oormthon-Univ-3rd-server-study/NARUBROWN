package me.na2ru2.narubrown.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.na2ru2.narubrown.security.jwt.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private static final String URI = "/auth/success";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(authentication.getName());
        String accessToken = jwtProvider.createAccessToken(authentication.getName());

        response.addCookie(createCookie(accessToken));
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .build().toUriString();
        response.sendRedirect(redirectUrl);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("Authorization", value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
