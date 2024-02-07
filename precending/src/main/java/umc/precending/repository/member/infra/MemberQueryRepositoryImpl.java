package umc.precending.repository.member.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.member.Authority;
import umc.precending.dto.member.MemberResponseDto;
import umc.precending.dto.member.QMemberResponseDto;

import java.util.List;

import static umc.precending.domain.member.QMember.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryRepositoryImpl implements MemberQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberResponseDto> findMembersByAuthorityAndName(Authority authority, String name) {
        return jpaQueryFactory.select(getResponseType())
                .from(member)
                .where(member.authority.eq(authority).and(member.name.contains(name)))
                .orderBy(member.name.desc())
                .fetch();
    }

    private QMemberResponseDto getResponseType() {
        return new QMemberResponseDto(member.name, member.introduction, member.images);
    }
}