package mushroom.schedule.task;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableAsync
public class Scheduling {

    private final RabbitTemplate rabbitTemplate;

    @Async
    @Scheduled(cron = "0 */1 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void startAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("startAuctions() start time: {}", now);
        String sendNow = now.toString();
        rabbitTemplate.convertAndSend("scheduling_task", "key_StartAuctions", sendNow);
        log.info("startAuctions() end time: {}", now);
    }

    @Async
    @Scheduled(cron = "0 */1 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void completeAuctions() {
        LocalDateTime now = LocalDateTime.now();
        String sendNow = now.toString();
        log.info("completeAuctions() start time: {}", sendNow);
        rabbitTemplate.convertAndSend("scheduling_task", "key_completeAuctions", sendNow);
        log.info("completeAuctions() end time: {}", sendNow);
    }

    @Async
    @Scheduled(cron = "0 */1 * * * *")
    public void createNoticeStartTime() {
        LocalDateTime now = LocalDateTime.now();
        log.info("createNoticeStartTime() start time: {}", now);
        String sendNow = now.toString();
        rabbitTemplate.convertAndSend("scheduling_task", "key_createNoticeStartTime", sendNow);
        log.info("createNoticeStartTime() end time: {}", now);
    }
    @Async
    @Scheduled(cron = "0 */1 * * * *")
    public void createNoticeEndTime() {
        LocalDateTime now = LocalDateTime.now();
        log.info("createNoticeEndTime() start time: {}", now);
        String sendNow = now.toString();
        rabbitTemplate.convertAndSend("scheduling_task", "key_CreateNoticeEndTime", sendNow);
        log.info("createNoticeEndTime() end time: {}", now);
    }

}
