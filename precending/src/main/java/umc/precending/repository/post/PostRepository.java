package umc.precending.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.precending.domain.post.Post;
import umc.precending.repository.post.infra.PostQueryRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {
}