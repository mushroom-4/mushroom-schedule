package mushroom.schedule.domain.auction_item.repository;

import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long>,
    AuctionItemRepositoryCustom {

}
