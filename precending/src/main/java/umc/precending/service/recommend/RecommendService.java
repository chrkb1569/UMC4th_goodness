package umc.precending.service.recommend;

import org.springframework.stereotype.Service;
import umc.precending.domain.member.Member;
import umc.precending.domain.recommend.Recommend;

import java.util.List;

@Service
public class RecommendService {
    public List<String> recommendRandomPreceding(Member member) {
        return Recommend.getRandomRecommendList();
    }
}