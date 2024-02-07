package umc.precending.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.domain.member.Member;
import umc.precending.dto.member.MemberInfoDto;
import umc.precending.dto.member.MemberResponseDto;
import umc.precending.dto.member.MemberUpdateDto;
import umc.precending.response.Response;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.member.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "사용자 API", description = "사용자 정보 수정 및 탈퇴와 관련된 로직을 수행하기 위한 Controller")
public class MemberController {
    private final MemberFindService memberFindService;
    private final MemberService memberService;

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "사용자 정보 조회", description = "사용자의 정보를 조회하는 API")
    public Response getMemberInfo() {
        Member findMember = getMember();
        MemberInfoDto memberInfo = memberService.getMemberInfo(findMember);

        return Response.success(memberInfo);
    }

    @GetMapping("/members/corporate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "기업 회원 검색", description = "기업 이름을 통하여 회원을 검색하는 API")
    public Response findCorporateMember(@RequestParam("name") String name) {
        Member findMember = getMember();
        List<MemberResponseDto> memberList = memberService.findCorporateMember(findMember, name);

        return Response.success(memberList);
    }

    @GetMapping("/members/club")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "동이리 회원 검색", description = "동아리 이름을 통하여 회원을 검색하는 API")
    public Response findClubMember(@RequestParam("name") String name) {
        Member findMember = getMember();
        List<MemberResponseDto> memberList = memberService.findClubMember(findMember, name);

        return Response.success(memberList);
    }

    @PutMapping("/members/image")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "사용자 이미지 수정", description = "사용자의 이미지를 수정하는 API")
    public void updateMemberImage(@ModelAttribute MultipartFile file) {
        Member findMember = getMember();
        memberService.editMemberImage(file, findMember);
    }

    @PutMapping("/members/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "사용자 정보 수정", description = "소개글이나 사용자의 이름을 수정하기 위한 API")
    public void updateMemberInfo(@Valid @RequestBody MemberUpdateDto request) {
        Member member = getMember();
        memberService.updateMember(member, request);
    }

    @DeleteMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "사용자 정보 삭제", description = "사용자의 정보를 삭제하기 위한 API")
    public void deleteMemberInfo() {
        Member member = getMember();
        memberService.deleteMember(member);
    }

    // 현재 로그인하고 있는 사용자의 정보를 통해 정보 반환
    private Member getMember() {
        return memberFindService.findCurrentMember();
    }
}