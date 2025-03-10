package mushroom.schedule.domain.like.repository;



import static mushroom.schedule.domain.auction_item.entity.QAuctionItem.auctionItem;
import static mushroom.schedule.domain.like.entity.QAuctionItemLike.auctionItemLike;
import static mushroom.schedule.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mushroom.schedule.domain.notice.dto.NoticeRes;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class AuctionItemLikeRepositoryImpl implements AuctionItemLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<NoticeRes> findNoticeInfoOfStartByAuctionItemLike(LocalDateTime now,
        LocalDateTime nowPlus10) {
        return queryFactory
            .select(Projections.constructor(NoticeRes.class,
                auctionItem, user, auctionItemLike))
            .from(auctionItemLike)
            .innerJoin(auctionItemLike.auctionItem, auctionItem)
            .innerJoin(auctionItemLike.user, user)
            .fetchJoin()
            .where(
                // 현재 시간과 startTime 비교 // 현재 시간+10분 과 startTime 비교
                auctionItem.startTime.gt(now).and(auctionItem.startTime.loe(nowPlus10)),
                auctionItem.isDeleted.isFalse() // 삭제되지 않은 항목만 검색
            ).fetch();
    }

    @Override
    public List<NoticeRes> findNoticeInfoOfEndByAuctionItemLike(LocalDateTime now,
        LocalDateTime nowPlus10) {
        return queryFactory
            .select(Projections.constructor(NoticeRes.class,
                auctionItem, user, auctionItemLike))
            .from(auctionItemLike)
            .innerJoin(auctionItemLike.auctionItem, auctionItem)
            .innerJoin(auctionItemLike.user, user)
            .where(
                // 현재 시간과 endTime 비교 // 현재 시간+10분 과 endTime 비교
                auctionItem.endTime.gt(now).and(auctionItem.endTime.loe(nowPlus10)),
                auctionItem.isDeleted.isFalse() // 삭제되지 않은 항목만 검색
            ).fetch();
    }

}
