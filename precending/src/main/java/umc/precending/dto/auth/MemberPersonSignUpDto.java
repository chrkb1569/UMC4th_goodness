package umc.precending.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberPersonSignUpDto {
    @NotBlank(message = "회원가입에 필요한 이름을 입력해주세요.")
    @Schema(description = "회원가입에 필요한 사용자의 이름", example = "이민섭")
    private String name;

    @NotBlank(message = "회원가입에 필요한 이메일을 입력해주세요.")
    @Schema(description = "회원가입에 필요한 이메일", example = "test@test.com")
    private String email;

    @NotBlank(message = "회원가입에 필요한 비밀번호를 입력해주세요.")
    @Length(min = 8, max = 16, message = "비밀번호는 최소 8자, 최대 16자로 구성되어야합니다.")
    @Schema(description = "회원가입에 필요한 비밀번호", example = "123456789", minLength = 8, maxLength = 16)
    private String password;
}
