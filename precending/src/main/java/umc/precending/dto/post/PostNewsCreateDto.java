package umc.precending.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostNewsCreateDto {
    @NotBlank(message = "선행을 기록하기 위한 기사 링크를 입력해주세요.")
    @Schema(description = "선행과 관련된 기사의 링크(다음, 네이버 기사만 지원합니다.)", example = "https://www.test.com")
    private String newsUrl;

    @NotNull(message = "선행을 수행한 연도를 입력해주세요")
    @Schema(description = "선행을 수행한 일자 - 연도", example = "9999")
    private Integer year;

    @NotNull(message = "선행을 수행한 월을 입력해주세요")
    @Schema(description = "선행을 수행한 일자 - 월", example = "12")
    private Integer month;

    @NotNull(message = "선행을 수행한 일자를 입력해주세요")
    @Schema(description = "선행을 수행한 일자 - 일", example = "31")
    private Integer day;

    @NotNull(message = "선행을 기록하기 위한 카테고리를 입력해주세요.")
    @Schema(description = "선행과 관련된 카테고리", example = "배려, 양보")
    List<String> categories = new ArrayList<>();
}