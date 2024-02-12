package umc.precending.dto.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.question.Question;
import umc.precending.dto.answer.AnswerDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionResponseDto {
    private String title;
    private String content;
    private String writer;
    private LocalDateTime firstCreateDate;
    private LocalDateTime lastModifiedDate;
    private List<AnswerDto> answers = new ArrayList<>();

    public QuestionResponseDto(Question question) {
        this.title = question.getTitle();
        this.content = question.getContent();
        this.writer = question.getWriter();
        this.firstCreateDate = question.getFirstCreatedDate();
        this.lastModifiedDate = question.getLastModifiedDate();
        this.answers = question.getAnswers().stream().map(AnswerDto::toDto).toList();
    }
}