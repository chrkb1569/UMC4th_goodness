package umc.precending.controller.cheer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.precending.domain.member.Member;
import umc.precending.dto.cheer.CheerResponseDto;
import umc.precending.response.Response;
import umc.precending.service.cheer.CheerService;
import umc.precending.service.member.MemberFindService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "응원 API", description = "개인 회원이 기업이나 동아리를 응원하는 로직을 수행하기 위한 Controller입니다.")
public class CheerController {
    private final MemberFindService memberFindService;
    private final CheerService cheerService;

    @GetMapping("/cheers/corporate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "사용자가 응원한 기업 조회", description = "사용자가 응원한 기업 회원들을 조회하는 API")
    public Response getCheeredCorporate() {
        Member findMember = getMember();
        List<String> responseData = cheerService.getCheeringCorporate(findMember);

        return Response.success(responseData);
    }

    @GetMapping("/cheers/club")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "사용자가 응원한 동아리 조회", description = "사용자가 응원한 동아리 회원들을 조회하는 API")
    public Response getCheeredClub() {
        Member findMember = getMember();
        List<String> responseData = cheerService.getCheeringClub(findMember);

        return Response.success(responseData);
    }

    @GetMapping("/cheers/top/corporate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "가장 많이 응원받은 기업 조회 - TOP5", description = "사용자들에게 가장 많은 응원을 받은 기업들을 조회하는 API")
    public Response getMostCorporate() {
        Member findMember = getMember();
        List<CheerResponseDto> responseData = cheerService.getMostCorporateMember(findMember);

        return Response.success(responseData);
    }

    @GetMapping("/cheers/top/club")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "가장 많이 응원받은 동아리 조회 - TOP5", description = "사용자들에게 가장 많은 응원을 받은 동아리를 조회하는 API")
    public Response getMostClub() {
        Member findMember = getMember();
        List<CheerResponseDto> responseData = cheerService.getMostClubMember(findMember);

        return Response.success(responseData);
    }

    @PostMapping("/cheers/corporate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "기업 응원", description = "특정 기업을 응원하기 위한 API")
    public void cheerCorporate(@PathVariable Long id) {
        Member findMember = getMember();
        cheerService.cheerCorporateMember(findMember, id);
    }

    @PostMapping("/cheers/club/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "동아리 응원", description = "특정 동아리를 응원하기 위한 API")
    public void cheerClub(@PathVariable Long id) {
        Member findMember = getMember();
        cheerService.cheerClubMember(findMember, id);
    }

    @DeleteMapping("/cheers/corporate/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "기업 응원 취소", description = "특정 기업에 대한 응원을 취소하는 API")
    public void disposeCorporateCheer(@PathVariable Long id) {
        Member findMember = getMember();

        cheerService.disposeCorporateCheer(findMember, id);
    }

    @DeleteMapping("/cheers/club/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "동아리 응원 취소", description = "특정 동아리에 대한 응원을 취소하는 API")
    public void disposeClubCheer(@PathVariable Long id) {
        Member findMember = getMember();

        cheerService.disposeClubCheer(findMember, id);
    }

    private Member getMember() {
        return memberFindService.findCurrentMember();
    }
}