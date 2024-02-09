package umc.precending.controller.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.precending.domain.member.Member;
import umc.precending.dto.post.PostEditDto;
import umc.precending.dto.post.PostNewsCreateDto;
import umc.precending.dto.post.PostNewsEditDto;
import umc.precending.dto.post.PostNormalCreateDto;
import umc.precending.response.Response;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.post.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "게시글 관련 API", description = "게시글 작성, 조회, 수정, 삭제와 관련된 로직을 수행하기 위한 Controller입니다.")
public class PostController {
    private final MemberFindService memberFindService;
    private final PostService postService;

    @GetMapping("/posts/{year}/{month}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "사용자 전체 게시글 조회 - 선행 일자 기준", description = "사용자의 선행 일자를 기준으로 게시글을 조회하는 API")
    public Response findAll(@PathVariable int year, @PathVariable int month) {
        Member findMember = getMember();
        return Response.success(postService.findAll(year, month, findMember));
    }

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "단일 게시글 조회", description = "PK값을 통하여 특정 게시글을 조회하는 API")
    public Response findOne(@PathVariable Long id) {
        return Response.success(postService.findOne(id));
    }

    @GetMapping("/posts/corporate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "기업 작성 게시글 조회", description = "기업 회원이 작성한 게시글들을 조회하는 API")
    public Response findCorporatePost() {
        return Response.success(postService.getCorporatePost());
    }

    @GetMapping("/posts/club")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "동아리 작성 게시글 조회", description = "동아리 회원이 작성한 게시글들을 조회하는 API")
    public Response findClubPost() {
        return Response.success(postService.getClubPost());
    }

    @GetMapping("/admin/posts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "인증 가능한 게시글 조회 - 관리자용", description = "관리자가 인증 가능한 게시글들을 조회하는 API")
    public Response findValidPosts() {
        return Response.success(postService.findVerifiablePost());
    }

    @PostMapping("/posts/valid/normal")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "인증 가능한 게시글 생성 - 일반", description = "사진을 첨부하여 인증 가능한 게시글을 작성하는 API")
    public void makeValidatePost(@ModelAttribute @Valid PostNormalCreateDto createDto) {
        Member findMember = getMember();
        postService.makeValidateNormalPost(createDto, findMember);
    }

    @PostMapping("/posts/valid/news")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "인증 가능한 게시글 생성 - 뉴스", description = "뉴스 링크를 첨부하여 인증 가능한 게시글을 작성하는 API")
    public void makeValidateNews(@ModelAttribute @Valid PostNewsCreateDto createDto) {
        Member findMember = getMember();
        postService.makeValidateNews(createDto, findMember);
    }

    @PostMapping("/posts/invalid/normal")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "인증 불가능한 게시글 생성", description = "인증이 불가능한 게시글을 작성하는 API")
    public void makeNormalPost(@ModelAttribute @Valid PostNormalCreateDto createDto) {
        Member findMember = getMember();
        postService.makeNormalPost(createDto, findMember);
    }

    @PutMapping("/admin/posts/valid/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "게시글 인증 - 관리자용", description = "인증 가능한 게시글을 관리자가 판단하기 위한 API")
    public void checkPostValidation(@PathVariable Long id) {
        postService.checkPostValidation(id);
    }

    @PutMapping("/posts/normal/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "일반 게시글 수정", description = "일반 게시글을 수정하기 위한 API")
    public void editNormalPost(@ModelAttribute @Valid PostEditDto editDto, @PathVariable Long id) {
        Member findMember = getMember();
        postService.editNormalPost(editDto, findMember, id);
    }

    @PutMapping("/posts/news/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "뉴스 게시글 수정", description = "뉴스 게시글을 수정하기 위한 API")
    public void editNewsPost(@ModelAttribute @Valid PostNewsEditDto editDto, @PathVariable Long id) {
        Member findMember = getMember();
        postService.editNewsPost(editDto, findMember, id);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "게시글 삭제", description = "게시글을 삭제하기 위한 API")
    public void deletePost(@PathVariable Long id) {
        Member findMember = getMember();
        postService.deletePost(findMember, id);
    }

    private Member getMember() {
        return memberFindService.findCurrentMember();
    }
}