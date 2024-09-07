package me.na2ru2.narubrown.post.controller;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.post.dto.req.PostReqDto;
import me.na2ru2.narubrown.post.dto.res.PostResDto;
import me.na2ru2.narubrown.post.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public PostResDto findPost(@RequestParam Long id) {
        return postService.findPost(id);
    }

    @PostMapping("/")
    public void save(@RequestBody PostReqDto postReqDto, @RequestParam Long user_id) {
        postService.save(postReqDto, user_id);
    }
}
