package mushroom.schedule.domain.bid.repository;


import mushroom.schedule.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {

}
