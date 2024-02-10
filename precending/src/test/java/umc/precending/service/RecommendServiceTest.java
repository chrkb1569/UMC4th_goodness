package umc.precending.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.precending.domain.member.Member;
import umc.precending.service.recommend.RecommendService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static umc.precending.factory.MemberFactory.*;

@DisplayName("Recommend [Service Layer] -> RecommendService 테스트")
public class RecommendServiceTest {
    private final RecommendService recommendService = new RecommendService();

    @Test
    @DisplayName("사용자에게 임의로 선행 추천")
    public void recommendRandomPrecedingTest() {
        // given
        Member currentMember = MEMBER_1.getPersonalMemberInstance();

        // when
        List<String> responseData = recommendService.recommendRandomPreceding(currentMember);

        // then
        assertThat(responseData.isEmpty()).isFalse();
    }
}