package me.na2ru2.narubrown.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {
    // 모든 Emitters 를 저장하는 ConcurrentHashMap
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public void deleteById(String id) {
        emitters.remove(id);
    }

    public SseEmitter get(String email) {
        return emitters.get(email);
    }
}
