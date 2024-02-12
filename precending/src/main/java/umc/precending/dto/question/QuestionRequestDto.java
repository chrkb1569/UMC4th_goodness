package umc.precending.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {
    @NotBlank(message = "문의 사항의 제목을 입력해주세요")
    @Schema(description = "문의 사항 제목", example = "test")
    private String title;

    @NotBlank(message = "문의 사항의 내용을 입력해주세요")
    @Schema(description = "문의 사항 내용", example = "test")
    private String content;
}