package mushroom.schedule.domain.like.repository;

import mushroom.schedule.domain.like.entity.AuctionItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemLikeRepository extends JpaRepository<AuctionItemLike, Long>,
    AuctionItemLikeRepositoryCustom {

}
