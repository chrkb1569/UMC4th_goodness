package umc.precending.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.precending.domain.member.Member;
import umc.precending.repository.member.infra.MemberQueryRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
    Optional<Member> findMemberByUsername(String username);
    boolean existsMemberByEmail(String email);
}