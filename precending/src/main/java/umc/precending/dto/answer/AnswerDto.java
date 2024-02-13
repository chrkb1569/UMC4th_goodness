package umc.precending.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.answer.Answer;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private String content;
    private String writer;
    private LocalDateTime firstCreatedDate;
    private LocalDateTime lastModifiedDate;

    private AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.writer = answer.getWriter();
        this.firstCreatedDate = answer.getFirstCreatedDate();
        this.lastModifiedDate = answer.getLastModifiedDate();
    }

    public static AnswerDto toDto(Answer answer) {
        return new AnswerDto(answer);
    }
}
