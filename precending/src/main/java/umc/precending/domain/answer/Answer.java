package umc.precending.domain.answer;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import umc.precending.domain.base.BaseEntity;
import umc.precending.domain.question.Question;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "ANSWER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    public Answer(String content, String writer, Question question) {
        this.content = content;
        this.writer = writer;
        this.question = question;
    }

    public void editContent(String content) {
        this.content = content;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}