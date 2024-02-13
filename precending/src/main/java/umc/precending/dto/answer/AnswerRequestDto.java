package umc.precending.dto.answer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequestDto {
    @NotBlank(message = "답변을 작성하기 위하여 내용을 입력해주세요")
    @Schema(description = "답변으로 작성할 내용", example = "test")
    private String content;
}
