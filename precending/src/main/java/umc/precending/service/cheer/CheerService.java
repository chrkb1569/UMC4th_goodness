package umc.precending.service.cheer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.cheer.Cheer;
import umc.precending.domain.member.Authority;
import umc.precending.domain.member.Club;
import umc.precending.domain.member.Corporate;
import umc.precending.domain.member.Member;
import umc.precending.dto.cheer.CheerResponseDto;
import umc.precending.exception.cheer.CheerListEmptyException;
import umc.precending.exception.cheer.CheerMemberNotFoundException;
import umc.precending.exception.cheer.CheerNotFoundException;
import umc.precending.exception.member.MemberRoleException;
import umc.precending.repository.cheer.CheerRepository;
import umc.precending.service.member.MemberFindService;

import java.util.List;

import static umc.precending.domain.member.Authority.*;

@Service
@RequiredArgsConstructor
public class CheerService {
    private final MemberFindService memberFindService;
    private final CheerRepository cheerRepository;

    @Transactional(readOnly = true)
    public List<String> getCheeringCorporate(Member member) {
        checkMemberValidation(member, ROLE_PERSON);

        List<String> corporateList = cheerRepository.findCheeringMemberList(member, ROLE_CORPORATE);
        if(corporateList.isEmpty()) throw new CheerMemberNotFoundException();

        return corporateList;
    }

    @Transactional(readOnly = true)
    public List<String> getCheeringClub(Member member) {
        checkMemberValidation(member, ROLE_PERSON);

        List<String> clubList = cheerRepository.findCheeringMemberList(member, ROLE_CLUB);
        if(clubList.isEmpty()) throw new CheerMemberNotFoundException();

        return clubList;
    }

    @Transactional(readOnly = true)
    public List<CheerResponseDto> getMostCorporateMember(Member member) {
        List<CheerResponseDto> topMemberList = cheerRepository.findTopCheeringMember(ROLE_CORPORATE);
        if(topMemberList.isEmpty()) throw new CheerListEmptyException();

        return topMemberList;
    }

    @Transactional(readOnly = true)
    public List<CheerResponseDto> getMostClubMember(Member member) {
        List<CheerResponseDto> topMemberList = cheerRepository.findTopCheeringMember(ROLE_CLUB);
        if(topMemberList.isEmpty()) throw new CheerListEmptyException();

        return topMemberList;
    }

    @Transactional
    public void cheerCorporateMember(Member member, Long corporateId) {
        checkMemberValidation(member, ROLE_PERSON);

        Member findMember = findMember(corporateId);
        checkMemberValidation(findMember, ROLE_CORPORATE);

        Cheer cheer = getCheerInstance(member, findMember);
        cheerOrganization(findMember);

        cheerRepository.save(cheer);
    }

    @Transactional
    public void cheerClubMember(Member member, Long clubId) {
        checkMemberValidation(member, ROLE_PERSON);

        Member findMember = findMember(clubId);
        checkMemberValidation(findMember, ROLE_CLUB);

        Cheer cheer = getCheerInstance(member, findMember);
        cheerOrganization(findMember);

        cheerRepository.save(cheer);
    }

    @Transactional
    public void disposeCorporateCheer(Member member, Long cheerId) {
        checkMemberValidation(member, ROLE_PERSON);

        Cheer findCheer = findCheerById(cheerId);

        checkCheerAndMemberValidation(findCheer, member, ROLE_CORPORATE);
        disposeCheer(findCheer.getOrganizationMember());

        cheerRepository.delete(findCheer);
    }

    @Transactional
    public void disposeClubCheer(Member member, Long cheerId) {
        checkMemberValidation(member, ROLE_PERSON);

        Cheer findCheer = findCheerById(cheerId);

        checkCheerAndMemberValidation(findCheer, member, ROLE_CLUB);
        disposeCheer(findCheer.getOrganizationMember());

        cheerRepository.delete(findCheer);
    }

    private void checkMemberValidation(Member member, Authority authority) {
        if(!member.getAuthority().equals(authority)) throw new MemberRoleException();
    }

    private void checkCheerAndMemberValidation(Cheer cheer, Member member, Authority authority) {
        if(!cheer.getPersonalMember().getUsername().equals(member.getUsername()))
            throw new MemberRoleException();
        checkMemberValidation(cheer.getOrganizationMember(), authority);
    }

    private Member findMember(Long id) {
        return memberFindService.findMemberById(id);
    }

    private Cheer getCheerInstance(Member personalMember, Member organizationMember) {
        return new Cheer(personalMember, organizationMember);
    }

    private <T extends Member> void cheerOrganization(T organization) {
        if(organization instanceof Corporate) {
            ((Corporate)organization).increaseScore();
            return;
        }

        ((Club)organization).increaseScore();
    }

    private Cheer findCheerById(Long cheerId) {
        return cheerRepository.findById(cheerId).orElseThrow(CheerNotFoundException::new);
    }

    private <T extends Member> void disposeCheer(T organization) {
        if(organization instanceof Corporate) {
            ((Corporate)organization).decreaseScore();
            return;
        }

        ((Club)organization).decreaseScore();
    }
}