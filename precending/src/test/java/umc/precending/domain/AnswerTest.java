package umc.precending.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.precending.domain.answer.Answer;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.factory.AnswerFactory;

import static org.assertj.core.api.Assertions.*;
import static umc.precending.factory.AnswerFactory.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.QuestionFactory.*;

@DisplayName("Answer 도메인 테스트")
public class AnswerTest {
    private Answer answer;

    @Test
    @DisplayName("Answer Domain 객체를 생성한다.")
    public void getAnswerInstance() {
        // given
        Member member = MEMBER_1.getPersonalMemberInstance();
        Question question = QUESTION_1.getQuestionInstance(member);
        AnswerFactory factory = ANSWER_1;

        // when
        answer = factory.getAnswerInstance(member, question);

        // then
        Assertions.assertAll(
                () -> assertThat(answer.getContent()).isEqualTo(factory.getContent()),
                () -> assertThat(answer.getWriter()).isEqualTo(member.getUsername()),
                () -> assertThat(answer.getQuestion()).isEqualTo(question)
        );
    }
}