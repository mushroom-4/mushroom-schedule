package mushroom.schedule.domain.bid.repository;


import java.util.List;
import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.bid.entity.Bid;

public interface BidRepositoryCustom {

    Bid findPotentiallySucceededBidByAuctionItem(AuctionItem auctionItem);

    List<Bid> findPotentiallyFailedBidsByAuctionItem(AuctionItem auctionItem);

    Boolean existsBidByAuctionItem(AuctionItem auctionItem);

}
