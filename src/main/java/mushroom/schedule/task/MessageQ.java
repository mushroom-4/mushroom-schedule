package mushroom.schedule.task;


import static mushroom.schedule.domain.auction_item.entity.AuctionItemStatus.PROGRESSING;
import static mushroom.schedule.domain.auction_item.entity.AuctionItemStatus.WAITING;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.auction_item.repository.AuctionItemRepository;
import mushroom.schedule.domain.bid.entity.Bid;
import mushroom.schedule.domain.bid.repository.BidRepository;
import mushroom.schedule.domain.like.repository.AuctionItemLikeRepository;
import mushroom.schedule.domain.notice.dto.NoticeRes;
import mushroom.schedule.domain.notice.entity.Notice;
import mushroom.schedule.domain.notice.entity.NoticeType;
import mushroom.schedule.domain.notice.repository.NoticeRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageQ {

    private final AuctionItemRepository auctionItemRepository;
    private final BidRepository bidRepository;
    private final NoticeRepository noticeRepository;
    private final AuctionItemLikeRepository auctionItemLikeRepository;

    @RabbitListener(queues = "startAuctions")
    @Transactional(readOnly = false)
    public void directQueueStartAuctions(String receivedString) {

        try {

            log.info("StartAuctions :: receivedNow: {}", receivedString);

            LocalDateTime now = LocalDateTime.parse(receivedString).truncatedTo(ChronoUnit.MINUTES);

            List<AuctionItem> waitingAuctionItems = auctionItemRepository
                .findAuctionItemsByStatusAndStartTime(WAITING, now);
            log.info("auctionItem count : {}", waitingAuctionItems.size());

            for (AuctionItem auctionItem : waitingAuctionItems) {
                log.info("auctionItem ID : {}", auctionItem.getId());
                auctionItem.start();
                log.info("auctionItem status : {}", auctionItem.getStatus());
            }
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("end : {}", end.truncatedTo(ChronoUnit.MINUTES));
    }
    @Transactional(readOnly = false)
    @RabbitListener(queues = "completeAuctions")
    public void directQueueCompleteAuctions(String receivedString) {

        try {
            log.info("CompleteAuctions :: receivedNow: {}", receivedString);

            LocalDateTime now = LocalDateTime.parse(receivedString).truncatedTo(ChronoUnit.MINUTES);

            List<AuctionItem> progressingAuctionItems = auctionItemRepository.findAuctionItemsByStatusAndEndTime(
                PROGRESSING, now);

            for (AuctionItem auctionItem : progressingAuctionItems) {
                log.info("auction endTime : {}", auctionItem.getEndTime());

                if (Boolean.FALSE.equals(bidRepository.existsBidByAuctionItem(auctionItem))) {
                    auctionItem.nonTrade();
                    log.info("auction non-trade id : {}", auctionItem.getId());
                    log.info("auction non-trade Status : {}", auctionItem.getStatus());
                    continue;
                }

                log.info("auction ID : {}", auctionItem.getId().toString());
                auctionItem.complete();
                log.info("auction Status : {}", auctionItem.getStatus());

                Bid succedBid = bidRepository.findPotentiallySucceededBidByAuctionItem(auctionItem);
                log.info("succeedBid ID : {}", succedBid.getId().toString());
                succedBid.succeed();

                // 최고가 아닌 Bid들을 fail 처리
                bidRepository.findPotentiallyFailedBidsByAuctionItem(auctionItem)
                    .forEach(Bid::fail);
            }
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "createNoticeStartTime")
    @Transactional
    public void directQueueCreateNoticeStartTime(String receivedString) {

        try {
            log.info("CreateNoticeStartTime :: receivedNow: {}", receivedString);

            LocalDateTime now = LocalDateTime.parse(receivedString).truncatedTo(ChronoUnit.MINUTES);

            // 현재 시간에 10분 추가
            LocalDateTime nowPlus10 = now.plusMinutes(10);

            // findNoticeInfoForLike 매개변수로 경매 물품의 경매 시작 시간 비교군 now, nowPlus
            List<NoticeRes> noticeResList = auctionItemLikeRepository.findNoticeInfoOfStartByAuctionItemLike(
                now,
                nowPlus10);

            log.info(" start - noticeResList.count: {}", noticeResList.size());

            for (NoticeRes noticeRes : noticeResList) {
                noticeRepository.save(Notice.builder()
                    .auctionItem(noticeRes.auctionItem())
                    .user(noticeRes.user())
                    .auctionItemLike(noticeRes.auctionItemLike())
                    .noticeType(NoticeType.START_TIME)
                    .build());
            }
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
        }
    }
    @RabbitListener(queues = "createNoticeEndTime")
    @Transactional
    public void directQueueCreateNoticeEndTime(String receivedString) {

        try {
            log.info("createNoticeEndTime :: receivedNow: {}", receivedString);
            // 문자열을 LocalDateTime으로 변환
            LocalDateTime now = LocalDateTime.parse(receivedString).truncatedTo(ChronoUnit.MINUTES);

            // 현재 시간에 10분 추가
            LocalDateTime nowPlus10 = now.plusMinutes(10);


// findNoticeInfoForLike 매개변수로 경매 물품의 경매 시작 시간 비교군 now, nowPlus
            List<NoticeRes> noticeResList = auctionItemLikeRepository.findNoticeInfoOfEndByAuctionItemLike(now,
                nowPlus10);

            log.info(" end - noticeResList.count: {}", noticeResList.size());

            for (NoticeRes noticeRes : noticeResList) {
                noticeRepository.save(Notice.builder()
                    .auctionItem(noticeRes.auctionItem())
                    .user(noticeRes.user())
                    .auctionItemLike(noticeRes.auctionItemLike())
                    .noticeType(NoticeType.END_TIME)
                    .build());
            }
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
        }
    }
}



