package me.na2ru2.narubrown.user.contoller;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.user.dto.req.UserReqDto;
import me.na2ru2.narubrown.user.dto.res.TokenResDto;
import me.na2ru2.narubrown.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserReqDto userReqDto) {
        Long result = userService.signUp(userReqDto);
        return result + "번 User가 생성되었습니다.";
    }

    @PostMapping("/sign-in")
    public TokenResDto signIn(@RequestBody UserReqDto userReqDto) {
        return userService.signIn(userReqDto);
    }
}
