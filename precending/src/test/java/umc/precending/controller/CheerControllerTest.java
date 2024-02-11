package umc.precending.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import umc.precending.controller.cheer.CheerController;
import umc.precending.domain.member.Member;
import umc.precending.dto.cheer.CheerResponseDto;
import umc.precending.service.cheer.CheerService;
import umc.precending.service.member.MemberFindService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static umc.precending.factory.MemberFactory.MEMBER_1;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cheer [Controller Layer] -> CheerController 테스트")
public class CheerControllerTest {
    @Mock
    private MemberFindService memberFindService;

    @Mock
    private CheerService cheerService;

    @InjectMocks
    private CheerController cheerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(cheerController)
                .build();
    }

    @Nested
    @DisplayName("사용자가 응원한 기업 조회 API [GET /api/cheers/corporate]")
    class getCheeredCorporateTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/corporate";

        @Test
        @DisplayName("응원한 기업 조회에 성공한다.")
        public void successGetCheeredCorporate() throws Exception {
            // given
            List<String> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(cheerService.getCheeringCorporate(currentMember)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk());

            // then
            verify(cheerService).getCheeringCorporate(currentMember);
        }
    }

    @Nested
    @DisplayName("사용자가 응원한 동아리 조회 API [GET /api/cheers/club]")
    class getCheeredClubTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/club";
        @Test
        @DisplayName("응원한 동아리 조회에 성공한다.")
        public void successGetCheeredClub() throws Exception {
            // given
            List<String> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(cheerService.getCheeringClub(currentMember)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());

            // then
            verify(cheerService).getCheeringClub(currentMember);
        }
    }

    @Nested
    @DisplayName("가장 많이 응원받은 기업 조회 API [GET /api/cheers/top/corporate]")
    class getMostCorporateTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/top/corporate";
        @Test
        @DisplayName("가장 많은 응원을 받은 기업 조회에 성공한다.")
        public void successGetMostCorporate() throws Exception {
            // given
            List<CheerResponseDto> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(cheerService.getMostCorporateMember(currentMember)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());

            // then
            verify(cheerService).getMostCorporateMember(currentMember);
        }
    }

    @Nested
    @DisplayName("가장 많이 응원받은 동아리 조회 API [GET /api//cheers/top/club]")
    class getMostClubTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api//cheers/top/club";

        @Test
        @DisplayName("가장 많은 응원을 받은 동아리 조회에 성공한다.")
        public void successGetMostClub() throws Exception {
            // given
            List<CheerResponseDto> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(cheerService.getMostClubMember(currentMember)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());

            // then
            verify(cheerService).getMostClubMember(currentMember);
        }
    }

    @Nested
    @DisplayName("기업 응원 API [POST /api/cheers/corporate/{id}]")
    class cheerCorporateTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/corporate/{id}";
        private final static long corporateId = 1L;

        @Test
        @DisplayName("기업 응원에 성공한다.")
        public void successCheerCorporate() throws Exception {
            // given

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(cheerService).cheerCorporateMember(currentMember, corporateId);

            // when
            mockMvc.perform(post(BASE_URL, corporateId)).andExpect(status().isCreated());

            // then
            verify(cheerService).cheerCorporateMember(currentMember, corporateId);
        }
    }

    @Nested
    @DisplayName("동아리 응원 API [POST /api/cheers/club/{id}]")
    class cheerClubTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/club/{id}";
        private final static long clubId = 1L;

        @Test
        @DisplayName("동아리 응원에 성공한다.")
        public void successCheerClub() throws Exception {
            // given

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(cheerService).cheerClubMember(currentMember, clubId);

            // when
            mockMvc.perform(post(BASE_URL, clubId)).andExpect(status().isCreated());

            // then
            verify(cheerService).cheerClubMember(currentMember, clubId);
        }
    }

    @Nested
    @DisplayName("기업 응원 취소 API [DELETE /api/cheers/corporate/{id}]")
    class disposeCorporateCheerTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/corporate/{id}";
        private final static long cheerId = 1L;

        @Test
        @DisplayName("기업 응원 취소에 성공한다.")
        public void successDisposeCorporateCheer() throws Exception {
            // given

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(cheerService).disposeCorporateCheer(currentMember, cheerId);

            // when
            mockMvc.perform(delete(BASE_URL, cheerId)).andExpect(status().isOk());

            // then
            verify(cheerService).disposeCorporateCheer(currentMember, cheerId);
        }
    }

    @Nested
    @DisplayName("동아리 응원 취소 API [DELETE /api/cheers/club/{id}]")
    class disposeClubCheerTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/cheers/club/{id}";
        private final static long cheerId = 1L;
        @Test
        @DisplayName("동아리 응원 취소에 성공한다.")
        public void successDisposeClubCheer() throws Exception {
            // given

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(cheerService).disposeClubCheer(currentMember, cheerId);

            // when
            mockMvc.perform(delete(BASE_URL, cheerId)).andExpect(status().isOk());

            // then
            verify(cheerService).disposeClubCheer(currentMember, cheerId);
        }
    }
}