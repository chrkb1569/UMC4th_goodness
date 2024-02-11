package umc.precending.repository.cheer;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.precending.domain.cheer.Cheer;
import umc.precending.repository.cheer.infra.CheerQueryRepository;

public interface CheerRepository extends JpaRepository<Cheer, Long>, CheerQueryRepository {
}