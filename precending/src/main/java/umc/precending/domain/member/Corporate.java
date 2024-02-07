package umc.precending.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Corporate extends Member {
    private final int INITIAL_SCORE = 0;

    @Column(name = "registrationNumber", nullable = false, unique = true)
    private String registrationNumber; // 사업자 등록 번호

    @Column(name="score",nullable=false)
    private int score; // 응원 점수

    public Corporate(String name, String password, String email, String registrationNumber) {
        super(name, password, email);
        this.registrationNumber = registrationNumber;
        this.authority = Authority.ROLE_CORPORATE;
        this.score = INITIAL_SCORE;
    }

    public void increaseScore() {
        this.score++;
    }

    public void decreaseScore() {
        this.score--;
    }
}