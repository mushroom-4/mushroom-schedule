package mushroom.schedule.domain.auction_item.repository;


import java.time.LocalDateTime;
import java.util.List;
import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.auction_item.entity.AuctionItemStatus;

public interface AuctionItemRepositoryCustom {

    List<AuctionItem> findAuctionItemsByStatusAndStartTime(AuctionItemStatus auctionItemStatus,
        LocalDateTime now);

    List<AuctionItem> findAuctionItemsByStatusAndEndTime(AuctionItemStatus auctionItemStatus,
        LocalDateTime now);

}
