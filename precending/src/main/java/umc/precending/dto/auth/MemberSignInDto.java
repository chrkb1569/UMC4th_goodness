package umc.precending.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberSignInDto {
    @NotBlank(message = "로그인을 수행하기 위한 아이디를 입력해주세요.")
    @Schema(description = "로그인에 필요한 사용자 아이디(가입 시 사용하였던 이메일 주소)", example = "test@test.com")
    private String username;

    @NotBlank(message = "로그인을 수행하기 위한 비밀번호를 입력해주세요.")
    @Schema(description = "로그인에 필요한 사용자 비밀번호", example = "1234567890")
    private String password;

    public UsernamePasswordAuthenticationToken getAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
