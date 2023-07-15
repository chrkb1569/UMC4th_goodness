package umc.precending.service.PersonService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.member.*;
import umc.precending.dto.person.NameScoreClubDto;
import umc.precending.dto.person.NameScoreCorporateDto;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.exception.person.PersonAddClubException;
import umc.precending.exception.person.PersonAddCorporateException;
import umc.precending.repository.member.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final CorporateRepository corporateRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final Person_CorporateRepository personCorporateRepository;
    private final Person_ClubRepository personClubRepository;
    @Transactional
    public void addScoreCorporate(String CorporateUsername){
        Member member1=memberRepository.findMemberByUsername(CorporateUsername).orElseThrow(MemberNotFoundException::new);
        Corporate corporate=(Corporate) member1;
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String personUsername=authentication.getName();
        Member member2=memberRepository.findMemberByUsername(personUsername).orElseThrow(MemberNotFoundException::new);
        Person person=(Person) member2;
        if(!personCorporateRepository.existsByPerson_IdAndCorporate_Id(person.getId(), corporate.getId())){
            person.addMyCorporate(corporate);
        }
        else{
            throw new PersonAddCorporateException();
        }
    }
    @Transactional
    public void addScoreClub(String ClubUsername){
        Member member1=memberRepository.findMemberByUsername(ClubUsername).orElseThrow(MemberNotFoundException::new);
        Club club=(Club) member1;
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String personUsername=authentication.getName();
        Member member2=memberRepository.findMemberByUsername(personUsername).orElseThrow(MemberNotFoundException::new);
        Person person=(Person) member2;
        if(!personClubRepository.existsByPerson_IdAndClub_Id(person.getId(), club.getId())){
            person.addMyClub(club);
        }
        else{
            throw new PersonAddClubException();
        }
    }

    public List<NameScoreCorporateDto> showMyCorporate(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String name=authentication.getName();
        Member member=memberRepository.findMemberByUsername(name).orElseThrow(MemberNotFoundException::new);
        Person person=(Person)member;
        Pageable pageable = PageRequest.of(0, 5, Sort.by("corporate.score").descending());
        List<Person_Corporate> personCorporates=personCorporateRepository.findTop5ByPersonIdOrderByCorporateScoreDesc(person.getId(),pageable);
        List<NameScoreCorporateDto> result=personCorporates.stream().map(c->new NameScoreCorporateDto(c.getCorporate())).collect(Collectors.toList());
        return result;
    }
    public List<NameScoreClubDto> showMyClub(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String name=authentication.getName();
        Member member=memberRepository.findMemberByUsername(name).orElseThrow(MemberNotFoundException::new);
        Person person=(Person)member;
        Pageable pageable = PageRequest.of(0, 5, Sort.by("club.score").descending());
        List<Person_Club> personClubs=personClubRepository.findTop5ByPersonIdOrderByClubScoreDesc(person.getId(),pageable);
        List<NameScoreClubDto> result=personClubs.stream().map(c->new NameScoreClubDto(c.getClub())).collect(Collectors.toList());
        return result;
    }
    public List<NameScoreCorporateDto> showHighestScoreCorporate(){
        List<Corporate> corporates=corporateRepository.findTop5ByScoreGreaterThanOrderByScoreDesc(0);
        List<NameScoreCorporateDto> result=corporates.stream().map(c->new NameScoreCorporateDto(c)).collect(Collectors.toList());
        return result;
    }
    public List<NameScoreClubDto> showHighestScoreClub(){
        List<Club> clubs=clubRepository.findTop5ByScoreGreaterThanOrderByScoreDesc(0);
        List<NameScoreClubDto> result=clubs.stream().map(c->new NameScoreClubDto(c)).collect(Collectors.toList());
        return result;
    }

}
