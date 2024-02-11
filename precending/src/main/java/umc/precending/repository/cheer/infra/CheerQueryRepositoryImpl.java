package umc.precending.repository.cheer.infra;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.member.*;
import umc.precending.dto.cheer.CheerResponseDto;
import umc.precending.dto.cheer.QCheerResponseDto;

import java.util.List;

import static umc.precending.domain.cheer.QCheer.*;
import static umc.precending.domain.member.QClub.*;
import static umc.precending.domain.member.QCorporate.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheerQueryRepositoryImpl implements CheerQueryRepository {
    private static final int MAX_PAGE = 5;
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<String> findCheeringMemberList(Member member, Authority authority) {
        return jpaQueryFactory
                .selectDistinct(cheer.organizationMember.name)
                .where(cheer.personalMember.username.eq(member.getUsername())
                        .and(cheer.organizationMember.authority.eq(authority)))
                .orderBy(cheer.organizationMember.name.asc())
                .limit(MAX_PAGE)
                .fetch();
    }

    @Override
    public List<CheerResponseDto> findTopCheeringMember(Authority authority) {
        return jpaQueryFactory
                .select(getResponseType(authority))
                .from(cheer)
                .where(cheer.organizationMember.authority.eq(authority))
                .orderBy(getOrderCondition(authority))
                .limit(MAX_PAGE)
                .fetch();
    }

    private QCheerResponseDto getResponseType(Authority authority) {
        if(authority.equals(Authority.ROLE_CORPORATE)) return new QCheerResponseDto(corporate.name, corporate.score);
        return new QCheerResponseDto(club.name, club.score);
    }

    private OrderSpecifier<?> getOrderCondition(Authority authority) {
        if(authority.equals(Authority.ROLE_CORPORATE)) return corporate.score.desc();
        return club.score.desc();
    }
}