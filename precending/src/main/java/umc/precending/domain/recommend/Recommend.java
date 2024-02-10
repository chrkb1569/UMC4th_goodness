package umc.precending.domain.recommend;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
public enum Recommend {
    RECOMMEND_DATA_1("기부를 해보신 적이 있으신가요? 만약 처음이라면, 기부를 한 번 해보는 것은 어떨까요?"),
    RECOMMEND_DATA_2("사용하지 않는 물품들은 무료 나눔을 통하여 필요한 사람들에게 도움을 주세요!"),
    RECOMMEND_DATA_3("정기 후원을 실천해 보는 것은 어떨까요?"),
    RECOMMEND_DATA_4("소액 기부에 대해서 알고 계셨나요? 적은 금액으로도 나눔을 실천할 수 있어요!"),
    RECOMMEND_DATA_5("오늘 시간이 남는다면 봉사활동에 참여해 보는 것은 어떨까요?"),
    RECOMMEND_DATA_6("무거운 짐을 들고 계시는 어르신을 도와드려 봐요!"),
    RECOMMEND_DATA_7("가끔은 지인이나 동료들에게 먼저 음료를 건네보는 것은 어떨까요?"),
    RECOMMEND_DATA_8("누군가의 고민을 들어주는 것도 그 사람에게 큰 도움이 될 수 있어요"),
    RECOMMEND_DATA_9("대중교통에서 다른 사람들에게 자리를 양보해 봐요!"),
    RECOMMEND_DATA_10("다른 사람을 위하여 문을 잡아주는 것은 어떨까요?")
    ;

    private String recommendContent;

    Recommend(String recommendContent) {
        this.recommendContent = recommendContent;
    }

    public static List<String> getRandomRecommendList() {
        List<Integer> randomNumber = getRandomList();

        return randomNumber.stream()
                .map(index -> values()[index].getRecommendContent()).toList();
    }

    private static List<Integer> getRandomList() {
        List<Integer> randomList = new LinkedList<>();
        int MIN_INDEX = 0;
        int MAX_INDEX = 3;

        for(int randomNumber = 0; randomNumber < values().length; randomNumber++) {
            randomList.add(randomNumber);
        }

        Collections.shuffle(randomList);

        return randomList.subList(MIN_INDEX, MAX_INDEX);
    }
}