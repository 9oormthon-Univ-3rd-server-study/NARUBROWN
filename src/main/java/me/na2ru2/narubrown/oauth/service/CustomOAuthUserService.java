package me.na2ru2.narubrown.oauth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.oauth.dto.OAuth2UserInfo;
import me.na2ru2.narubrown.user.domain.User;
import me.na2ru2.narubrown.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo attributes = OAuth2UserInfo.of(provider, oAuth2User.getAttributes());
        Optional<User> user = userRepository.findByEmail(attributes.email());
        if (user.isEmpty()) {
            userRepository.save(attributes.toEntity());
        }
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new DefaultOAuth2User(authorities, attributes.attributes(), attributes.nameAttributesKey());

    }
}
