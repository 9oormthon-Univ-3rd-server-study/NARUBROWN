package me.na2ru2.narubrown.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.na2ru2.narubrown.notification.repository.EmitterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    // 기본 타임 아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    /*
      클라이언트가 구독을 위해 호출하는 메서드
      @param userId - 구동하는 클라이언트의 사용자 아이디
      @return SeeEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(String userEmail) {
        // 매 연결마다 고유 이벤트 id부여

        SseEmitter emitter = this.createEmitter(userEmail);
        // 첫 연결 시 503 Service 방지용 더미 Event 전송
        this.sendToClient(userEmail, userEmail + ": EventStream Created.");

        return emitter;
    }

    /*
    서버의 이벤트를 클라이언트에게 보내는 메서드
    다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
    @param eventId - 메세지를 전송할 이벤트 ID
    @param event - 전송할 이벤트 객체.
     */
    public void notify(String eventId, Object event) {
        this.sendToClient(eventId, event);
    }

    /*
    * 클라이언트에게 데이터를 전송
    * @param id - 데이터를 받을 사용자의 아이디
    * @param data - 전송할 데이터
    * */
    private void sendToClient(String id, Object data) {
        log.info(id + " 짜짜짠");
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(
                        SseEmitter
                                .event()
                                .id(id)
                                .name("sse")
                                .data(data)
                );
            } catch (IOException e) {
               emitterRepository.deleteById(id);
               emitter.completeWithError(e);
            }
        }
    }

    /*
    사용자 아이디를 기반으로 이벤트 Emitter를 생성
    @param id - 사용자 id
    @return SseEmitter - 생성된 이벤트 Emitter
     */
    private SseEmitter createEmitter(String id) {
        // SSE EMITTER 객체 생성
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        // 객체 저장
        emitterRepository.save(id, emitter);

        // 특정한 상황이 발생했을 때 비동기적으로 처리되는 코드이다.
        // Emitter가 완료될 때 (모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() ->
                emitterRepository.deleteById(id));
        // Emitter가 타임 아웃 되었을 때 (지정된 시간 동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() ->
                emitterRepository.deleteById(id));

        return emitter;
    }
}
