package mushroom.schedule.domain.auction_item.repository;


import static mushroom.schedule.domain.auction_item.entity.QAuctionItem.auctionItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.auction_item.entity.AuctionItemStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionItemRepositoryImpl implements AuctionItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuctionItem> findAuctionItemsByStatusAndStartTime(
        AuctionItemStatus auctionItemStatus,
        LocalDateTime now) {
        return queryFactory.select(auctionItem)
            .from(auctionItem)
            .where(
                auctionItem.status.eq(auctionItemStatus),
                auctionItem.startTime.between(now, now.withSecond(59)),
                auctionItem.isDeleted.isFalse()
            )
            .fetch();
    }

    @Override
    public List<AuctionItem> findAuctionItemsByStatusAndEndTime(AuctionItemStatus auctionItemStatus,
        LocalDateTime now) {
        return queryFactory.select(auctionItem)
            .from(auctionItem)
            .where(
                auctionItem.status.eq(auctionItemStatus),
                auctionItem.endTime.between(now, now.withSecond(59)),
                auctionItem.isDeleted.isFalse()
            )
            .fetch();
    }
}
