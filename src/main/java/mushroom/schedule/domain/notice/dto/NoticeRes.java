package mushroom.schedule.domain.notice.dto;

import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.like.entity.AuctionItemLike;
import mushroom.schedule.domain.user.entity.User;

public record NoticeRes(
    AuctionItem auctionItem,
    User user,
    AuctionItemLike auctionItemLike
) {

}
