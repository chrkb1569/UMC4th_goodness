package umc.precending.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.precending.domain.answer.Answer;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.dto.answer.AnswerRequestDto;
import umc.precending.exception.answer.AnswerNotFoundException;
import umc.precending.exception.answer.AnswerNotMatchedWriterException;
import umc.precending.exception.question.QuestionNotFoundException;
import umc.precending.repository.answer.AnswerRepository;
import umc.precending.service.answer.AnswerService;
import umc.precending.service.question.QuestionFindService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static umc.precending.factory.AnswerFactory.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.QuestionFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Answer [Service Layer] -> AnswerService 테스트")
public class AnswerServiceTest {
    @Mock
    private QuestionFindService questionFindService;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerService answerService;

    @Nested
    @DisplayName("답변 생성")
    class createAnswerTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static Answer answer = ANSWER_1.getAnswerInstance(currentMember, question);
        private final static long questionId = 1L;
        private final static long errorId = -1L;

        @Test
        @DisplayName("유효하지 않은 PK값을 통하여 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            AnswerRequestDto requestDto = getRequestDto(answer.getContent());

            // stub
            willThrow(QuestionNotFoundException.class)
                    .given(questionFindService)
                    .findQuestionById(errorId);

            // when - then
            assertThatThrownBy(() -> answerService.createAnswer(requestDto, currentMember, errorId))
                    .isInstanceOf(QuestionNotFoundException.class);
        }

        @Test
        @DisplayName("답변 생성에 성공한다.")
        public void successCreateAnswer() {
            // given
            AnswerRequestDto requestDto = getRequestDto(answer.getContent());

            // stub
            given(questionFindService.findQuestionById(questionId)).willReturn(question);
            given(answerRepository.save(any())).willReturn(any());

            // when
            answerService.createAnswer(requestDto, currentMember, questionId);

            // then
            verify(questionFindService).findQuestionById(questionId);
            verify(answerRepository).save(any());
        }
    }

    @Nested
    @DisplayName("답변 수정")
    class editAnswerTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member otherMember = MEMBER_2.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static Answer answer = ANSWER_1.getAnswerInstance(currentMember, question);
        private final static long answerId = 1L;
        private final static long errorId = -1L;

        @Test
        @DisplayName("유효하지 않은 PK값을 통하여 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            AnswerRequestDto requestDto = getRequestDto(ANSWER_2.getContent());

            // stub
            given(answerRepository.findById(errorId)).willReturn(Optional.ofNullable(null));

            // when - then
            assertThatThrownBy(() -> answerService.editAnswer(requestDto, currentMember, errorId))
                    .isInstanceOf(AnswerNotFoundException.class);
        }

        @Test
        @DisplayName("작성자와 현재 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidMember() {
            // given
            AnswerRequestDto requestDto = getRequestDto(ANSWER_2.getContent());

            // stub
            given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

            // when - then
            assertThatThrownBy(() -> answerService.editAnswer(requestDto, otherMember, answerId))
                    .isInstanceOf(AnswerNotMatchedWriterException.class);
        }

        @Test
        @DisplayName("답변 수정에 성공한다.")
        public void successEditAnswer() {
            // given
            AnswerRequestDto requestDto = getRequestDto(ANSWER_2.getContent());

            // stub
            given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

            // when
            answerService.editAnswer(requestDto, currentMember, answerId);

            // then
            verify(answerRepository).findById(answerId);
            assertThat(answer.getContent()).isEqualTo(ANSWER_2.getContent());
        }
    }

    @Nested
    @DisplayName("답변 삭제")
    class deleteAnswerTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member otherMember = MEMBER_2.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static Answer answer = ANSWER_1.getAnswerInstance(currentMember, question);
        private final static long answerId = 1L;
        private final static long errorId = -1L;

        @Test
        @DisplayName("유효하지 않은 PK값을 통하여 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            given(answerRepository.findById(errorId)).willReturn(Optional.ofNullable(null));

            // when - then
            assertThatThrownBy(() -> answerService.deleteAnswer(currentMember, errorId))
                    .isInstanceOf(AnswerNotFoundException.class);
        }

        @Test
        @DisplayName("작성자와 현재 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidMember() {
            // given

            // stub
            given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

            // when - then
            assertThatThrownBy(() -> answerService.deleteAnswer(otherMember, answerId))
                    .isInstanceOf(AnswerNotMatchedWriterException.class);
        }

        @Test
        @DisplayName("답변 삭제에 성공한다.")
        public void successDeleteAnswer() {
            // given

            // stub
            given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
            willDoNothing().given(answerRepository).delete(any());

            // when
            answerService.deleteAnswer(currentMember, answerId);

            // then
            verify(answerRepository).findById(answerId);
            verify(answerRepository).delete(any());
        }
    }

    private AnswerRequestDto getRequestDto(String content) {
        return new AnswerRequestDto(content);
    }
}