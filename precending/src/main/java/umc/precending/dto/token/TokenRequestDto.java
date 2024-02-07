package umc.precending.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenRequestDto {
    @NotBlank(message = "토큰 재발급을 위하여 accessToken의 값을 입력해주세요.")
    @Schema(description = "토큰 재발급을 위한 accessToken", example = "accessToken")
    private String accessToken;

    @NotBlank(message = "토큰 재발급을 위하여 refreshToken의 값을 입력해주세요.")
    @Schema(description = "토큰 재발급을 위한 refreshToken", example = "refreshToken")
    private String refreshToken;
}