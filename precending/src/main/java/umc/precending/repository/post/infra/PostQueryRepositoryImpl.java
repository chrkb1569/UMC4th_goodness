package umc.precending.repository.post.infra;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.member.Authority;
import umc.precending.domain.member.Member;
import umc.precending.dto.post.PostResponseDto;
import umc.precending.dto.post.QPostResponseDto;

import java.util.List;

import static umc.precending.domain.member.QMember.*;
import static umc.precending.domain.post.QPost.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryRepositoryImpl implements PostQueryRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<PostResponseDto> findPostsByPrecedingDate(Member member, int year, int month) {
        return queryFactory.select(getResponseType())
                .from(post)
                .where(post.writer.eq(member.getUsername())
                        .and(post.precedingDate.year.eq(year)
                                .and(post.precedingDate.month.eq(month))))
                .fetch();
    }

    @Override
    public List<PostResponseDto> findPostsByCorporate() {
        return queryFactory.select(getResponseType())
                .from(post)
                .where(findMemberByName(post.writer).eq(Authority.ROLE_CORPORATE))
                .orderBy(post.firstCreatedDate.desc())
                .fetch();
    }

    @Override
    public List<PostResponseDto> findPostsByClub() {
        return queryFactory.select(getResponseType())
                .from(post)
                .where(findMemberByName(post.writer).eq(Authority.ROLE_CLUB))
                .orderBy(post.firstCreatedDate.desc())
                .fetch();
    }

    @Override
    public List<PostResponseDto> findVerifiablePosts() {
        return queryFactory.select(getResponseType())
                .from(post)
                .where(post.verifiable.isTrue().and(post.isVerified.isFalse()))
                .fetch();
    }

    private QPostResponseDto getResponseType() {
        return new QPostResponseDto(post.id, post.content, post.firstCreatedDate,
                post.lastModifiedDate, post.verifiable, post.isVerified,
                post.imageList, post.postCategory);
    }

    private JPAQuery<Authority> findMemberByName(StringPath username) {
        return queryFactory.select(member.authority)
                .from(member)
                .where(member.username.eq(username));
    }
}