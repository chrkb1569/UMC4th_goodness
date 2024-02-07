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
public class Club extends Member {
    private final int INITIAL_SCORE = 0;

    @Column(name = "school", nullable = false)
    private String school; // 동아리가 속해있는 학교

    @Column(name = "address", nullable = false)
    private String address; // 동아리 주소

    @Column(name="score",nullable=false)
    private int score; // 응원 점수

    public Club(String name, String password, String email, String school, String address) {
        super(name, password, email);
        this.authority = Authority.ROLE_CLUB;
        this.school = school;
        this.address = address;
        this.score = INITIAL_SCORE;
    }

    public void increaseScore() {
        this.score++;
    }

    public void decreaseScore() {
        this.score--;
    }
}