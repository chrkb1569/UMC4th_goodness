package umc.precending.controller.notice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.precending.dto.notice.NoticeDetailedResponseDto;
import umc.precending.dto.notice.NoticeRequestDto;
import umc.precending.dto.notice.NoticeResponseDto;
import umc.precending.response.Response;
import umc.precending.service.notice.NoticeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "공지 사항 API", description = "공지 사항과 관련된 생성, 조회, 수정, 삭제 기능들을 수행하기 위한 Controller 입니다.")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/notices")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "공지 사항 전체 조회", description = "모든 공지 사항을 조회하기 위한 API")
    public Response getNotices() {
        List<NoticeResponseDto> responseData = noticeService.getNotices();

        return Response.success(responseData);
    }

    @GetMapping("/notices/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "공지 사항 단일 조회", description = "단일 공지 사항을 조회하기 위한 API")
    public Response getNotice(@PathVariable Long id) {
        NoticeDetailedResponseDto responseData = noticeService.getNotice(id);

        return Response.success(responseData);
    }

    @PostMapping("/admin/notices")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "공지 사항 생성 - 관리자 전용", description = "공지 사항을 생성하기 위한 API")
    public void createNotice(@RequestBody @Valid NoticeRequestDto requestDto) {
        noticeService.createNotice(requestDto);
    }

    @PutMapping("/admin/notices/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "PUT", summary = "공지 사항 수정 - 관리자 전용", description = "공지 사항을 수정하기 위한 API")
    public void editNotice(@RequestBody @Valid NoticeRequestDto requestDto, @PathVariable Long id) {
        noticeService.editNotice(requestDto, id);
    }

    @DeleteMapping("/admin/notices/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "DELETE", summary = "공지 사항 삭제 - 관리자 전용", description = "공지 사항을 삭제하기 위한 API")
    public void deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
    }
}