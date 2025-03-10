package mushroom.schedule.domain.notice.repository;

import mushroom.schedule.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
