package umc.precending.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDto {
    @NotBlank(message = "공지 사항의 제목을 입력해주세요.")
    @Schema(description = "공지 사항의 제목", example = "test")
    private String title; // 공지 사항 제목

    @NotBlank(message = "공지 사항의 본문을 입력해주세요.")
    @Schema(description = "공지 사항의 본문", example = "test")
    private String content; // 공지 사항 본문
}