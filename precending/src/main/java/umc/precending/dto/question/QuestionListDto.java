package umc.precending.dto.question;

import lombok.Getter;
import umc.precending.domain.question.Question;

import java.time.LocalDateTime;

@Getter
public class QuestionListDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime firstCreatedDate;
    private LocalDateTime lastModifiedDate;

    public QuestionListDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.writer = question.getWriter();
        this.firstCreatedDate = question.getFirstCreatedDate();
        this.lastModifiedDate = question.getLastModifiedDate();
    }
}