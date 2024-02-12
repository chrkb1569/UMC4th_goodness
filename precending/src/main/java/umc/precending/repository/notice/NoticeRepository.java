package umc.precending.repository.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.precending.domain.notice.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}