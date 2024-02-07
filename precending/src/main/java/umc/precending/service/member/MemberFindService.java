package umc.precending.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.member.Authority;
import umc.precending.domain.member.Member;
import umc.precending.dto.member.MemberResponseDto;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.repository.member.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFindService {
    private final MemberRepository memberRepository;

    public Member findCurrentMember() {
        String username = getCurrentUsername();
        return memberRepository.findMemberByUsername(username).orElseThrow(MemberNotFoundException::new);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public List<MemberResponseDto> findMemberByNameAndAuthority(Authority authority, String name) {
        List<MemberResponseDto> memberList = memberRepository.findMembersByAuthorityAndName(authority, name);
        if(memberList.isEmpty()) throw new MemberNotFoundException();

        return memberList;
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}