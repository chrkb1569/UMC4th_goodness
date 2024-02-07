package umc.precending.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.domain.image.Image;
import umc.precending.domain.image.MemberImage;
import umc.precending.domain.member.Member;
import umc.precending.dto.member.MemberInfoDto;
import umc.precending.dto.member.MemberResponseDto;
import umc.precending.dto.member.MemberUpdateDto;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.repository.member.MemberRepository;
import umc.precending.service.image.ImageService;

import java.util.List;

import static umc.precending.domain.member.Authority.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberFindService memberFindService;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public MemberInfoDto getMemberInfo(Member member) {
        return null;
    }

    // 회원의 프로필 이미지를 변경하는 로직
    @Transactional
    public void editMemberImage(MultipartFile file, Member member) {
        Image image = getMemberImage(file);
        saveMemberImage(file, image);

        member.editImage(List.of((MemberImage) image));
    }

    // 조건에 일치하는 기업 회원들을 검색하는 로직
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findCorporateMember(Member member, String name) {
        List<MemberResponseDto> responseData = memberFindService.findMemberByNameAndAuthority(ROLE_CORPORATE, name);
        if(responseData.isEmpty()) throw new MemberNotFoundException();

        return responseData;
    }

    // 조건에 일치하는 동아리 회원들을 검색하는 로직
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findClubMember(Member member, String name) {
        List<MemberResponseDto> responseData = memberFindService.findMemberByNameAndAuthority(ROLE_CLUB, name);
        if(responseData.isEmpty()) throw new MemberNotFoundException();

        return responseData;
    }

    // 회원의 이름이나 소개글을 변경하는 로직
    @Transactional
    public void updateMember(Member member, MemberUpdateDto request) {
        updateMemberInfo(member, request);
    }

    // 회원을 탈퇴하는 로직
    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    private Image getMemberImage(MultipartFile file) {
        return new MemberImage(file);
    }

    private void saveMemberImage(MultipartFile file, Image image) {
        String storedName = image.getStoredName();

        String accessUrl = imageService.saveImage(file, storedName);
        image.setAccessUrl(accessUrl);
    }

    private void updateMemberInfo(Member member, MemberUpdateDto updateDto) {
        String name = updateDto.getName();
        String introduction = updateDto.getIntroduction();

        if(checkNameValidation(name) && checkIntroductionValidation(introduction)) return;

        if(checkNameValidation(name)) {
            member.editIntroduction(introduction);
            return;
        }

        if(checkIntroductionValidation(introduction)) {
            member.editName(name);
            return;
        }

        member.editName(name);
        member.editIntroduction(introduction);
    }

    private boolean checkNameValidation(String name) {
        return name.isBlank() || name.isEmpty();
    }

    private boolean checkIntroductionValidation(String introduction) {
        return introduction.isEmpty() || introduction.isBlank();
    }
}