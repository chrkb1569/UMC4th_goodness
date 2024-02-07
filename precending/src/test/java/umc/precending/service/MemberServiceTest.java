package umc.precending.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import umc.precending.domain.image.MemberImage;
import umc.precending.domain.member.Authority;
import umc.precending.domain.member.Member;
import umc.precending.dto.member.MemberResponseDto;
import umc.precending.dto.member.MemberUpdateDto;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.repository.member.MemberRepository;
import umc.precending.service.image.ImageService;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.member.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static umc.precending.factory.ImageFactory.*;
import static umc.precending.factory.MemberFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member [Service Layer] -> MemberService 테스트")
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberFindService memberFindService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("현재 사용자 정보 조회")
    class getMemberInfoTest {
        private final Member member = MEMBER_1.getPersonalMemberInstance();

        @Test
        @DisplayName("현재 사용자 정보를 조회한다")
        public void successGetMemberInfo() {

        }
    }

    @Nested
    @DisplayName("사용자 프로필 이미지 수정")
    class editMemberImageTest {
        private final Member member = MEMBER_1.getPersonalMemberInstance();
        private final List<MemberImage> images = new ArrayList<>();
        private final MockMultipartFile file = new MockMultipartFile("image.jpeg", "image.jpeg", "multipart/form-data", new byte[]{});
        @Test
        @DisplayName("사용자 프로필 이미지 수정에 성공한다")
        public void successEditMemberImage() {
            // given
            MemberImage image1 = IMAGE_1.getMemberImage();
            MemberImage image2 = IMAGE_2.getMemberImage();

            images.add(image1);
            images.add(image2);

            member.saveImage(images);

            // when
            memberService.editMemberImage(file, member);

            // then
            assertThat(member.getImages().size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("기업 회원 검색")
    class findCorporateMemberTest {
        private final Member member = MEMBER_1.getPersonalMemberInstance();
        private final Member corporateMember = MEMBER_2.getCorporateMemberInstance();

        @Test
        @DisplayName("기업 회원이 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // given
            String corporateMemberName = corporateMember.getName();
            List<MemberResponseDto> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findMemberByNameAndAuthority(Authority.ROLE_CORPORATE, corporateMemberName))
                    .willReturn(responseData);

            // when - then
            assertThatThrownBy(() -> memberService.findCorporateMember(member, corporateMemberName))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("기업 회원 검색에 성공한다")
        public void successFindCorporateMember() {
            // given
            String corporateMemberName = corporateMember.getName();
            List<MemberResponseDto> responseData = new ArrayList<>();
            responseData.add(new MemberResponseDto(corporateMember.getName(), corporateMember.getIntroduction(), corporateMember.getImages()));

            // stub
            given(memberFindService.findMemberByNameAndAuthority(Authority.ROLE_CORPORATE, corporateMemberName))
                    .willReturn(responseData);

            // when
            List<MemberResponseDto> corporateMembers = memberService.findCorporateMember(member, corporateMemberName);

            // then
            assertThat(corporateMembers.size()).isEqualTo(responseData.size());
        }
    }

    @Nested
    @DisplayName("동아리 회원 검색")
    class findClubMemberTest {
        private final Member member = MEMBER_1.getPersonalMemberInstance();
        private final Member clubMember = MEMBER_2.getClubMemberInstance();

        @Test
        @DisplayName("동아리 회원이 존재하지 않을 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // given
            String clubMemberName = clubMember.getName();
            List<MemberResponseDto> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findMemberByNameAndAuthority(Authority.ROLE_CLUB, clubMemberName))
                    .willReturn(responseData);

            // when - then
            assertThatThrownBy(() -> memberService.findClubMember(member, clubMemberName))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("동아리 회원 검색에 성공한다")
        public void successFindCorporateMember() {
            // given
            String clubMemberName = clubMember.getName();
            List<MemberResponseDto> responseData = new ArrayList<>();
            responseData.add(new MemberResponseDto(clubMember.getName(), clubMember.getIntroduction(), clubMember.getImages()));

            // stub
            given(memberFindService.findMemberByNameAndAuthority(Authority.ROLE_CLUB, clubMemberName))
                    .willReturn(responseData);

            // when
            List<MemberResponseDto> clubMembers = memberService.findClubMember(member, clubMemberName);

            // then
            assertThat(clubMembers.size()).isEqualTo(responseData.size());
        }
    }

    @Nested
    @DisplayName("회원 정보 수정")
    class updateMemberTest {
        private final Member member = MEMBER_1.getPersonalMemberInstance();
        private final Member otherMember = MEMBER_2.getPersonalMemberInstance();
        private final MemberUpdateDto updateDto = getUpdateDto(otherMember.getName(), otherMember.getIntroduction());

        @Test
        @DisplayName("사용자 정보 수정에 성공한다.")
        public void successUpdateMember() {
            // given
            // when
            memberService.updateMember(member, updateDto);

            // then
            Assertions.assertAll(
                    () -> assertThat(member.getName()).isEqualTo(otherMember.getName()),
                    () -> assertThat(member.getIntroduction()).isEqualTo(otherMember.getIntroduction())
            );
        }
    }

    @Nested
    @DisplayName("회원 정보 삭제")
    class deleteMemberTest {
        private final Member member = MEMBER_1.getPersonalMemberInstance();

        @Test
        @DisplayName("회원 정보 삭제에 성공한다.")
        public void successDeleteMember() {
            // given
            // when
            memberService.deleteMember(member);

            // then
            verify(memberRepository).delete(member);
        }
    }

    private MemberUpdateDto getUpdateDto(String name, String introduction) {
        return new MemberUpdateDto(name, introduction);
    }
}