package umc.precending.dto.cheer;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CheerResponseDto {
    private String name;
    private int score;

    @QueryProjection
    public CheerResponseDto(String name, int score) {
        this.name = name;
        this.score = score;
    }
}