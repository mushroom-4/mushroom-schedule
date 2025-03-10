package mushroom.schedule.domain.bid.repository;



import static mushroom.schedule.domain.bid.entity.QBid.bid;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.bid.entity.Bid;
import mushroom.schedule.domain.bid.entity.QBid;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Bid findPotentiallySucceededBidByAuctionItem(AuctionItem auctionItem) {
        return queryFactory
            .select(bid)
            .from(bid)
            .where(bid.auctionItem.eq(auctionItem))
            .orderBy(bid.biddingPrice.desc())
            .limit(1)
            .fetchOne();
    }


    @Override
    public List<Bid> findPotentiallyFailedBidsByAuctionItem(AuctionItem auctionItem) {

        QBid subBid = new QBid("subBid");

        return queryFactory
            .select(bid)
            .from(bid)
            .where(
                bid.auctionItem.eq(auctionItem),
                bid.biddingPrice.ne(JPAExpressions
                    .select(subBid.biddingPrice.max())
                    .from(subBid)
                    .where(subBid.auctionItem.eq(auctionItem))
                )
            )
            .fetch();
    }

    @Override
    public Boolean existsBidByAuctionItem(AuctionItem auctionItem) {
        return queryFactory
            .select(bid)
            .from(bid)
            .where(bid.auctionItem.eq(auctionItem))
            .fetchFirst() != null;
    }
}
