package umc.precending.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import umc.precending.controller.recommend.RecommendController;
import umc.precending.domain.member.Member;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.recommend.RecommendService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static umc.precending.factory.MemberFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Recommend [Controller Layer] -> RecommendController 테스트")
public class RecommendControllerTest {
    @Mock
    private RecommendService recommendService;

    @Mock
    private MemberFindService memberFindService;

    @InjectMocks
    private RecommendController recommendController;

    private MockMvc mockMvc;

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(recommendController)
                .build();
    }

    @Test
    @DisplayName("추천 선행 API [GET /api/recommends]")
    public void getRecommendListTest() throws Exception {
        // given
        String BASE_URL = "/api/recommends";
        Member currentMember = MEMBER_1.getPersonalMemberInstance();
        List<String> recommends = new ArrayList<>();

        // stub
        given(memberFindService.findCurrentMember()).willReturn(currentMember);
        given(recommendService.recommendRandomPreceding(currentMember)).willReturn(recommends);

        // when
        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());

        // then
        verify(recommendService).recommendRandomPreceding(currentMember);
    }
}