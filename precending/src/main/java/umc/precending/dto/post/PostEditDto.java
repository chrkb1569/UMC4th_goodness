package umc.precending.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostEditDto {
    @NotNull(message = "수정할 선행 내용을 입력해주세요.")
    @Schema(description = "게시글에 기입하기 위한 선행 내용", example = "test")
    private String content;

    @NotNull(message = "수정할 선행 일자(연도)를 입력해주세요.")
    @Schema(description = "선행을 수행한 일자 - 연도", example = "9999")
    private int year;

    @NotNull(message = "수정할 선행 일자(월)를 입력해주세요.")
    @Schema(description = "선행을 수행한 일자 - 월", example = "12")
    private int month;

    @NotNull(message = "수정할 선행 일자(일)를 입력해주세요.")
    @Schema(description = "선행을 수행한 일자 - 일", example = "31")
    private int day;

    @NotNull(message = "수정할 선행 카테고리를 입력해주세요.")
    @Schema(description = "선행과 관련된 카테고리", example = "배려, 양보")
    private List<String> categories = new ArrayList<>();

    @Schema(description = "선행과 관련된 이미지")
    private List<MultipartFile> files = new ArrayList<>();
}
