package me.na2ru2.narubrown.oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResDto {
    private Long id;
    private Properties properties;
    private Kakao_account kakao_account;

    @Getter
    @Setter
    public static class Properties {
        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }

    @Getter
    @Setter
    public static class Kakao_account {
        private String email;
        private String name;
        private Profile profile;
    }

    @Getter
    @Setter
    public static class Profile {
        private String nickname;
        private String profile_image_url;
    }
}
