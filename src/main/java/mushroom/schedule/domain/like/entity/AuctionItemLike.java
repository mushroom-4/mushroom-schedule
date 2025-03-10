package mushroom.schedule.domain.like.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mushroom.schedule.domain.auction_item.entity.AuctionItem;
import mushroom.schedule.domain.user.entity.User;


@Getter
@Entity
@Table(name = "`auction_item_like`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionItemLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_item_id", nullable = false)
    private AuctionItem auctionItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public AuctionItemLike(Long id, AuctionItem auctionItem, User user) {
        this.id = id;
        this.auctionItem = auctionItem;
        this.user = user;
    }
}
