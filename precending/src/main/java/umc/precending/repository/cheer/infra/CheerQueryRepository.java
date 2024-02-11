package umc.precending.repository.cheer.infra;

import umc.precending.domain.member.Authority;
import umc.precending.domain.member.Member;
import umc.precending.dto.cheer.CheerResponseDto;

import java.util.List;

public interface CheerQueryRepository {
    List<String> findCheeringMemberList(Member member, Authority authority);
    List<CheerResponseDto> findTopCheeringMember(Authority authority);
}