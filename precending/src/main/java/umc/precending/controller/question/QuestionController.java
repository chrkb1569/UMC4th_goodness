package umc.precending.controller.question;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.precending.domain.member.Member;
import umc.precending.dto.question.QuestionListDto;
import umc.precending.dto.question.QuestionRequestDto;
import umc.precending.dto.question.QuestionResponseDto;
import umc.precending.response.Response;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.question.QuestionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "문의 사항 API", description = "문의 사항과 관련된 작업을 수행하기 위한 Controller입니다.")
public class QuestionController {
    private final MemberFindService memberFindService;
    private final QuestionService questionService;

    @GetMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "전체 문의 사항 조회", description = "전체 문의 사항을 조회하기 위한 API")
    public Response getQuestions() {
        Member findMember = getMember();
        List<QuestionListDto> responseData = questionService.getAllQuestions(findMember);

        return Response.success(responseData);
    }

    @GetMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "단일 문의 사항 조회", description = "PK값을 통하여 특정 문의 사항을 조회하기 위한 API")
    public Response getQuestion(@PathVariable Long id) {
        Member findMember = getMember();
        QuestionResponseDto responseData = questionService.getQuestion(findMember, id);

        return Response.success(responseData);
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "문의 사항 생성", description = "문의 사항을 생성하기 위한 API")
    public void createQuestion(@RequestBody @Valid QuestionRequestDto requestDto) {
        Member findMember = getMember();
        questionService.createQuestion(requestDto, findMember);
    }

    @PutMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "문의 사항 수정", description = "문의 사항을 수정하기 위한 API")
    public void editQuestion(@RequestBody @Valid QuestionRequestDto requestDto, @PathVariable Long id) {
        Member findMember = getMember();
        questionService.editQuestion(requestDto, findMember, id);
    }

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "문의 사항 삭제", description = "특정 문의 사항을 삭제하기 위한 API")
    public void deleteQuestion(@PathVariable Long id) {
        Member findMember = getMember();
        questionService.deleteQuestion(findMember, id);
    }

    private Member getMember() {
        return memberFindService.findCurrentMember();
    }
}