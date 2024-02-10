package umc.precending.controller.recommend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import umc.precending.domain.member.Member;
import umc.precending.response.Response;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.recommend.RecommendService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "추천 선행 API", description = "사용자에게 임의로 선행을 추천해주기 위한 Controller 입니다.")
public class RecommendController {
    private final MemberFindService memberFindService;
    private final RecommendService recommendService;

    @GetMapping("/recommends")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "선행 추천", description = "사용자에게 임의의 선행을 3개 추천하기 위한 API")
    public Response getRecommendList() {
        Member findMember = getMember();
        List<String> responseData = recommendService.recommendRandomPreceding(findMember);

        return Response.success(responseData);
    }

    private Member getMember() {
        return memberFindService.findCurrentMember();
    }
}