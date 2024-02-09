package umc.precending.repository.post.infra;

import umc.precending.domain.member.Member;
import umc.precending.dto.post.PostResponseDto;

import java.util.List;

public interface PostQueryRepository {
    List<PostResponseDto> findPostsByPrecedingDate(Member member, int year, int month);
    List<PostResponseDto> findPostsByCorporate();
    List<PostResponseDto> findPostsByClub();
    List<PostResponseDto> findVerifiablePosts();
}
