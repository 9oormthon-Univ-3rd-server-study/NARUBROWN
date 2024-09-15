package me.na2ru2.narubrown.notification.controller;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.notification.service.NotificationService;
import me.na2ru2.narubrown.user.domain.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationSerivce;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            // @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            @AuthenticationPrincipal User user
            ) {
        return notificationSerivce.subscribe(user.getEmail());
    }
}
