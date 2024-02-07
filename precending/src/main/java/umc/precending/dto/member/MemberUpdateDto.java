package umc.precending.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    @Schema(description = "수정할 사용자의 이름", example = "test", nullable = true)
    private String name;

    @Schema(description = "수정할 사용자의 소개글", example = "test", nullable = true)
    private String introduction;
}