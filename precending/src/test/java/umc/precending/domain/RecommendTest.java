package umc.precending.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.precending.domain.recommend.Recommend;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Recommend 열겨형 클래스 테스트")
public class RecommendTest {
    @Test
    @DisplayName("getRandomRecommendList() 메서드 테스트")
    public void getRandomRecommendListTest() {
        // given

        // when
        List<String> randomList = Recommend.getRandomRecommendList();

        // then
        assertAll(
                () -> assertThat(randomList.isEmpty()).isFalse(),
                () -> assertThat(randomList.size()).isEqualTo(3),
                () -> assertThat(randomList.stream().distinct().count()).isEqualTo(randomList.size())
        );
    }
}