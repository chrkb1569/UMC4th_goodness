package umc.precending.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import umc.precending.dto.token.TokenDto;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {
    private final static String AUTHORIZATION_KEY = "auth";
    private final Long validationTime;
    private final Long refreshTokenValidationTime;
    private final String secret;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.validationTime}") Long validationTime) {
        this.secret = secret;
        this.validationTime = validationTime * 1000;
        this.refreshTokenValidationTime = validationTime * 2 * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] key_set = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(key_set);
    }

    // Authentication 객체를 통하여 토큰 생성
    public TokenDto createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .setExpiration(new Date(now + validationTime))
                .setSubject(authentication.getName())
                .claim(AUTHORIZATION_KEY, authorities)
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidationTime))
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .validationTime(validationTime)
                .type("Bearer ")
                .build();
    }

    // 토큰을 통하여 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseData(token).getBody();

        List<SimpleGrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseData(token);
            return true;
        } catch(MalformedJwtException | SecurityException e) {
            log.info("잘못된 형식의 토큰입니다.");
        } catch(ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
        } catch(UnsupportedJwtException e) {
            log.info("지원하지 않는 형식의 토큰입니다.");
        } catch(IllegalArgumentException e) {
            log.info("잘못된 토큰입니다.");
        }
        return false;
    }

    public Jws<Claims> parseData(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build().parseClaimsJws(token);
    }
}