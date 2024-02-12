package umc.precending.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.factory.QuestionFactory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.QuestionFactory.*;

@DisplayName("Question 도메인 테스트")
public class QuestionTest {
    private final Member member = MEMBER_1.getPersonalMemberInstance();
    private Question question;

    @Test
    @DisplayName("Question Domain 객체를 생성한다.")
    public void createInstanceTest() {
        // given
        QuestionFactory factory = QUESTION_1;

        // when
        question = factory.getQuestionInstance(member);

        // then
        assertAll(
                () -> assertThat(question.getTitle()).isEqualTo(factory.getTitle()),
                () -> assertThat(question.getContent()).isEqualTo(factory.getContent()),
                () -> assertThat(question.getWriter()).isEqualTo(member.getUsername()),
                () -> assertThat(question.getAnswers()).isEmpty()
        );
    }

    @Test
    @DisplayName("editQuestion() 메서드 테스트")
    public void editQuestionTest() {
        // given
        QuestionFactory factory = QUESTION_2;
        question = QUESTION_1.getQuestionInstance(member);

        // when
        question.editQuestion(factory.getTitle(), factory.getContent());

        // then
        assertAll(
                () -> assertThat(question.getTitle()).isEqualTo(factory.getTitle()),
                () -> assertThat(question.getContent()).isEqualTo(factory.getContent())
        );
    }
}
