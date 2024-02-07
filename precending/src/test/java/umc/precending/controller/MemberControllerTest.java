package umc.precending.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import umc.precending.controller.member.MemberController;
import umc.precending.domain.image.MemberImage;
import umc.precending.domain.member.Club;
import umc.precending.domain.member.Corporate;
import umc.precending.domain.member.Member;
import umc.precending.dto.member.MemberResponseDto;
import umc.precending.dto.member.MemberUpdateDto;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.member.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static umc.precending.factory.ImageFactory.*;
import static umc.precending.factory.MemberFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member [Controller Layer] -> MemberController 테스트")
public class MemberControllerTest {
    @Mock
    private MemberService memberService;

    @Mock
    private MemberFindService memberFindService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(memberController)
                .build();
    }

    @Nested
    @DisplayName("사용자 정보 조회 API [GET /api/members]")
    class getMemberInfoTest {
        private final static String BASE_URL = "/api/members";
        @Test
        @DisplayName("")
        public void successGetMemberInfo() throws Exception {
            // given

            // stub

            // when

            // then
        }
    }

    @Nested
    @DisplayName("기업 회원 검색 API [GET /api/members/corporate]")
    class findCorporateMemberTest {
        private final static String BASE_URL = "/api/members/corporate";
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member corporateMember = MEMBER_2.getCorporateMemberInstance();
        private final static String corporateName = corporateMember.getName();

        @Test
        @DisplayName("기업 회원 검색에 성공한다.")
        public void successFindCorporateMember() throws Exception {
            // given
            List<MemberResponseDto> corporateMembers = getCorporateMembers((Corporate)corporateMember);

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(memberService.findCorporateMember(currentMember, corporateName))
                    .willReturn(corporateMembers);

            // when
            mockMvc.perform(
                    MockMvcRequestBuilders.get(BASE_URL)
                            .param("name", corporateName))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(memberService).findCorporateMember(currentMember, corporateName);
        }
    }

    @Nested
    @DisplayName("동아리 회원 검색 API [GET /api/members/club]")
    class findClubMemberTest {
        private final static String BASE_URL = "/api/members/club";
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member clubMember = MEMBER_2.getClubMemberInstance();
        private final static String clubName = clubMember.getName();

        @Test
        @DisplayName("클럽 회원 검색에 성공한다.")
        public void successFindClubMember() throws Exception {
            // given
            List<MemberResponseDto> clubMembers = getClubMembers((Club)clubMember);

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(memberService.findClubMember(currentMember, clubName))
                    .willReturn(clubMembers);

            // when
            mockMvc.perform(
                    MockMvcRequestBuilders.get(BASE_URL)
                            .param("name", clubName))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(memberService).findClubMember(currentMember, clubName);
        }
    }

    @Nested
    @DisplayName("회원 이미지 수정 API [PUT /api/members/image]")
    class updateMemberImageTest {
        private final static String BASE_URL = "/api/members/image";
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static MemberImage memberImage = IMAGE_1.getMemberImage();

        @Test
        @DisplayName("회원 이미지 수정에 성공한다.")
        public void successUpdateMemberImage() throws Exception {
            // given
            MockMultipartFile file = new MockMultipartFile("file", memberImage.getOriginalName(),
                    "multipart/form-data", new byte[]{});


            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(memberService).editMemberImage(file, currentMember);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart(HttpMethod.PUT, BASE_URL)
                            .file(file)
            ).andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(memberService).editMemberImage(file, currentMember);
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 API [PUT /api/members/info]")
    class updateMemberInfoTest {
        private static final String BASE_URL = "/api/members/info";
        private static final Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private static final Member otherMember = MEMBER_2.getPersonalMemberInstance();

        @Test
        @DisplayName("회원 정보 수정에 성공한다")
        public void successUpdateMemberInfo() throws Exception {
            // given
            MemberUpdateDto updateDto = getMemberUpdateDto(otherMember);

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(memberService).updateMember(any(Member.class), any(MemberUpdateDto.class));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                    .put(BASE_URL)
                    .content(objectMapper.writeValueAsString(updateDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(memberService).updateMember(any(Member.class), any(MemberUpdateDto.class));
        }
    }

    @Nested
    @DisplayName("회원 정보 삭제 API [DELETE /api/members]")
    class deleteMemberInfoTest {
        private static final String BASE_URL = "/api/members";
        private static final Member currentMember = MEMBER_1.getPersonalMemberInstance();

        @Test
        @DisplayName("회원 정보 삭제에 성공한다")
        public void successDeleteMemberInfo() throws Exception {
            // given

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(memberService).deleteMember(currentMember);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                    .delete(BASE_URL))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(memberService).deleteMember(currentMember);
        }
    }

    private List<MemberResponseDto> getCorporateMembers(Corporate corporate) {
        List<MemberResponseDto> corporateMembers = new ArrayList<>();
        corporateMembers.add(new MemberResponseDto(corporate.getName(), corporate.getIntroduction(), corporate.getImages()));

        return corporateMembers;
    }

    private List<MemberResponseDto> getClubMembers(Club corporate) {
        List<MemberResponseDto> corporateMembers = new ArrayList<>();
        corporateMembers.add(new MemberResponseDto(corporate.getName(), corporate.getIntroduction(), corporate.getImages()));

        return corporateMembers;
    }

    private MemberUpdateDto getMemberUpdateDto(Member member) {
        return new MemberUpdateDto(member.getName(), member.getIntroduction());
    }
}