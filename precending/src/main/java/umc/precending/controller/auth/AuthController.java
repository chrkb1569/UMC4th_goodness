package umc.precending.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.precending.dto.auth.MemberClubSignUpDto;
import umc.precending.dto.auth.MemberCorporateSignUpDto;
import umc.precending.dto.auth.MemberPersonSignUpDto;
import umc.precending.dto.auth.MemberSignInDto;
import umc.precending.dto.token.TokenRequestDto;
import umc.precending.response.Response;
import umc.precending.service.auth.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "사용자 인증 API", description = "회원가입, 로그인과 같은 인증과 관련된 로직을 수행하기 위한 Controller입니다.")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/sign-up/person")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "개인 사용자 회원 가입", description = "개인 사용자가 회원 가입하기 위한 API")
    public void signUp(@RequestBody @Valid MemberPersonSignUpDto signUpDto) {
        authService.signUp(signUpDto);
    }

    @PostMapping("/auth/sign-up/corporate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "기업 사용자 회원 가입", description = "기업이 회원 가입하기 위한 API")
    public void signUp(@RequestBody @Valid MemberCorporateSignUpDto signUpDto) {
        authService.signUp(signUpDto);
    }

    @PostMapping("/auth/sign-up/club")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "동아리 사용자 회원 가입", description = "동아리가 회원 가입하기 위한 API")
    public void signUp(@RequestBody @Valid MemberClubSignUpDto signUpDto) {
        authService.signUp(signUpDto);
    }

    @PostMapping("/auth/sign-in")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "POST", summary = "로그인", description = "로그인을 수행하기 위한 API")
    public Response signIn(@RequestBody @Valid MemberSignInDto signInDto) {
        return Response.success(authService.signIn(signInDto));
    }

    @PostMapping("/auth/reissue")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "POST", summary = "토큰 재발급", description = "유효기간이 만료된 JWT 토큰을 재발급받기 위한 API")
    public Response reIssue(@RequestBody @Valid TokenRequestDto requestDto) {
        return Response.success(authService.reIssue(requestDto));
    }
}
