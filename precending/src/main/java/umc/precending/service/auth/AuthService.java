package umc.precending.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.config.jwt.TokenProvider;
import umc.precending.domain.Recommend.Recommend;
import umc.precending.domain.member.Club;
import umc.precending.domain.member.Corporate;
import umc.precending.domain.member.Member;
import umc.precending.domain.member.Person;
import umc.precending.dto.auth.MemberClubSignUpDto;
import umc.precending.dto.auth.MemberCorporateSignUpDto;
import umc.precending.dto.auth.MemberPersonSignUpDto;
import umc.precending.dto.auth.MemberSignInDto;
import umc.precending.dto.token.TokenDto;
import umc.precending.dto.token.TokenRequestDto;
import umc.precending.dto.token.TokenResponseDto;
import umc.precending.exception.member.MemberDuplicateException;
import umc.precending.exception.member.MemberLoginFailureException;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.repository.recommendRepository.RecommendRepository;
import umc.precending.repository.member.*;
import umc.precending.service.email.MailService;
import umc.precending.service.redis.RedisService;

import javax.mail.MessagingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PersonRepository personRepository;
    private final CorporateRepository corporateRepository;
    private final ClubRepository clubRepository;
    private final RedisService redisService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RecommendRepository recommendRepository;
    private final MailService mailService;

    // 회원가입 - 개인
    @Transactional
    public void signUp(MemberPersonSignUpDto signUpDto) {
        Person personMember = getPersonMember(signUpDto);
        List<Recommend> recommends=recommendRepository.selectRandom();
        personMember.setMyTodayRecommendList(recommends);
        memberRepository.save(personMember);
    }

    // 회원가입 - 동아리
    @Transactional
    public void signUp(MemberClubSignUpDto signUpDto) {
        Club clubMember = getClubMember(signUpDto);
        List<Recommend> recommends=recommendRepository.selectRandom();
        memberRepository.save(clubMember);
    }

    //회원가입 - 기업
    @Transactional
    public void signUp(MemberCorporateSignUpDto signUpDto) {
        Corporate corporateMember = getCorporateMember(signUpDto);
        List<Recommend> recommends=recommendRepository.selectRandom();
        memberRepository.save(corporateMember);
    }

    // 로그인
    @Transactional
    public TokenResponseDto signIn(MemberSignInDto signInDto) {
        checkLoginValidation(signInDto);

        UsernamePasswordAuthenticationToken authenticationToken = signInDto.getAuthenticationToken();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenValue = tokenProvider.createToken(authentication);
        redisService.saveValue(authentication.getName(), tokenValue.getRefreshToken());

        return new TokenResponseDto(tokenValue.getAccessToken(), tokenValue.getRefreshToken());
    }

    // 만료기간이 다 된 토큰을 재발급받는 로직
    @Transactional
    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        redisService.checkValidation(authentication.getName(), refreshToken);

        TokenDto token = tokenProvider.createToken(authentication);

        redisService.saveValue(authentication.getName(), token.getRefreshToken());

        return new TokenResponseDto(token.getAccessToken(), token.getRefreshToken());
    }

    // 사용자가 로그인을 수행하면서 입력한 정보와 일치하는 계정이 있는지 확인하는 로직
    private void checkLoginValidation(MemberSignInDto signInDto) {
        Member findMember = memberRepository.findMemberByUsername(signInDto.getUsername())
                .orElseThrow(MemberNotFoundException::new);

        if(!checkPwValidation(findMember, signInDto.getPassword())) throw new MemberLoginFailureException();
    }

    // 비밀번호가 일치하는지 확인하는 로직
    private boolean checkPwValidation(Member member, String raw) {
        return passwordEncoder.matches(raw, member.getPassword());
    }

    // 입력한 정보를 바탕으로 유효성을 검사한 뒤, Person 객체를 반환하는 로직
    private Person getPersonMember(MemberPersonSignUpDto signUpDto) {
        if(personRepository.existsPersonByPhone(signUpDto.getPhone())) throw new MemberDuplicateException();

        return new Person(signUpDto.getName(), signUpDto.getBirth(),
                passwordEncoder.encode(signUpDto.getPassword()), signUpDto.getEmail(), signUpDto.getPhone());
    }

    // 입력한 정보를 바탕으로 유효성을 검사한 뒤, Club 객체를 반환하는 로직
    private Club getClubMember(MemberClubSignUpDto signUpDto) {
        if(clubRepository.existsById(100L)) throw new MemberDuplicateException();

        return new Club(signUpDto.getName(), signUpDto.getBirth(), passwordEncoder.encode(signUpDto.getPassword()),
                signUpDto.getEmail(), signUpDto.getType(), signUpDto.getSchool(), signUpDto.getAddress());
    }

    // 입력한 정보를 바탕으로 유효성을 검사한 뒤, Corporate 객체를 반환하는 로직
    private Corporate getCorporateMember(MemberCorporateSignUpDto signUpDto) {
        if(corporateRepository.existsCorporateByRegistrationNumber(signUpDto.getRegistrationNumber()))
            throw new MemberDuplicateException();

        return new Corporate(signUpDto.getName(), signUpDto.getBirth(), passwordEncoder.encode(signUpDto.getPassword()),
                signUpDto.getEmail(), signUpDto.getRegistrationNumber());
    }

    // 비밀번호 재설정 링크를 이메일로 보내기 위한 로직
    public String forgotPassword(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        try {
            mailService.sendSetPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("비밀번호 재설정 이메일을 보낼 수 없습니다. 다시 시도해주세요.");
        }
        return "please check your email to set new password to your account";
    }

    // 비밀번호 재설정 로직
    public String setPassword(String email, String newPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        String encodePassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodePassword);
        memberRepository.save(member);
        return "New password set successfully login with new password";
    }

    // 탈퇴
    @Transactional
    public String deleteMember(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
        return "회원 탈퇴가 완료되었습니다.";
    }
}
