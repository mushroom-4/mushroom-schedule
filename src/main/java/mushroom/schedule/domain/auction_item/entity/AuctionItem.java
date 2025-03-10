package mushroom.schedule.domain.auction_item.entity;



import static mushroom.schedule.common.exception.ExceptionType.INVALID_AUCTION_ITEM_STATUS;
import static mushroom.schedule.domain.auction_item.entity.AuctionItemStatus.INSPECTING;
import static mushroom.schedule.domain.auction_item.entity.AuctionItemStatus.PROGRESSING;
import static mushroom.schedule.domain.auction_item.entity.AuctionItemStatus.WAITING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mushroom.schedule.common.exception.CustomException;
import mushroom.schedule.domain.user.entity.User;


@Getter
@Entity
@Table(name = "`auction_item`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionItem extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url", length = 150)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private AuctionItemSize size;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private AuctionItemCategory category;

    @Column(name = "brand", length = 50, nullable = false)
    private String brand;

    @Column(name = "start_price", nullable = false)
    private Long startPrice;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuctionItemStatus status = INSPECTING;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Builder
    public AuctionItem(Long id, User seller, String name, String description, String imageUrl,
        AuctionItemSize size, AuctionItemCategory category, String brand, Long startPrice,
        LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.size = size;
        this.category = category;
        this.brand = brand;
        this.startPrice = startPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void start() {
        if (this.status != WAITING) {
            throw new CustomException(INVALID_AUCTION_ITEM_STATUS);
        }
        this.status = PROGRESSING;
    }

    public void complete() {
        if (this.status != PROGRESSING) {
            throw new CustomException(INVALID_AUCTION_ITEM_STATUS);
        }
        this.status = AuctionItemStatus.COMPLETED;
    }

    public void nonTrade() { // TODO 카멡케이스로 바꿈
        if (this.status != PROGRESSING) {
            throw new CustomException(INVALID_AUCTION_ITEM_STATUS);
        }
        this.status = AuctionItemStatus.NON_TRADED;
    }
}
