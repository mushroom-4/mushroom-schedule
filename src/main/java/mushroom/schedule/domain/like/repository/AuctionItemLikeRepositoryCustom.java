package mushroom.schedule.domain.like.repository;

import java.time.LocalDateTime;
import java.util.List;
import mushroom.schedule.domain.notice.dto.NoticeRes;

public interface AuctionItemLikeRepositoryCustom {

    List<NoticeRes> findNoticeInfoOfStartByAuctionItemLike(LocalDateTime now,
        LocalDateTime nowPlus);

    List<NoticeRes> findNoticeInfoOfEndByAuctionItemLike(LocalDateTime now,
        LocalDateTime nowPlus10);

}
