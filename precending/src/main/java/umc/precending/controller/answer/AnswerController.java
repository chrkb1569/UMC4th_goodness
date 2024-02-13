package umc.precending.controller.answer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.precending.domain.member.Member;
import umc.precending.dto.answer.AnswerRequestDto;
import umc.precending.service.answer.AnswerService;
import umc.precending.service.member.MemberFindService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "답변 API", description = "문의 사항에 대한 답변을 관리하기 위한 Controller 입니다.")
public class AnswerController {
    private final MemberFindService memberFindService;
    private final AnswerService answerService;

    @PostMapping("/answers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "답변 생성", description = "문의 사항에 대한 답변을 등록하기 위한 API")
    public void createAnswer(@RequestBody @Valid AnswerRequestDto requestDto,
                             @RequestParam Long questionId) {
        Member findMember = getMember();
        answerService.createAnswer(requestDto, findMember, questionId);
    }

    @PutMapping("/answers/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "답변 수정", description = "문의 사항에 대한 답변을 수정하기 위한 API")
    public void editAnswer(@RequestBody @Valid AnswerRequestDto requestDto, @PathVariable Long id) {
        Member findMember = getMember();
        answerService.editAnswer(requestDto, findMember, id);
    }

    @DeleteMapping("/answers/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "답변 삭제", description = "문의 사항에 대한 답변을 삭제하기 위한 API")
    public void deleteAnswer(@PathVariable Long id) {
        Member findMember = getMember();
        answerService.deleteAnswer(findMember, id);
    }

    private Member getMember() {
        return memberFindService.findCurrentMember();
    }
}