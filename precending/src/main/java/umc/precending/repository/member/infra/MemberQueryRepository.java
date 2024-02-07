package umc.precending.repository.member.infra;

import umc.precending.domain.member.Authority;
import umc.precending.dto.member.MemberResponseDto;

import java.util.List;

public interface MemberQueryRepository {
    List<MemberResponseDto> findMembersByAuthorityAndName(Authority authority, String name);
}
