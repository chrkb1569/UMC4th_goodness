package umc.precending.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.dto.question.QuestionRequestDto;
import umc.precending.dto.question.QuestionResponseDto;
import umc.precending.exception.question.QuestionNotFoundException;
import umc.precending.exception.question.QuestionNotMatchedWriterException;
import umc.precending.repository.question.QuestionRepository;
import umc.precending.service.question.QuestionFindService;
import umc.precending.service.question.QuestionService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.QuestionFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Question [Service Layer] -> QuestionService 테스트")
public class QuestionServiceTest {
    @Mock
    private QuestionFindService questionFindService;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Nested
    @DisplayName("문의 사항 전체 조회")
    class getAllQuestionsTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();

        @Test
        @DisplayName("조회한 결과가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<Question> questions = new ArrayList<>();

            // stub
            given(questionRepository.findAll()).willReturn(questions);

            // when - then
            assertThatThrownBy(() -> questionService.getAllQuestions(currentMember))
                    .isInstanceOf(QuestionNotFoundException.class);
        }

        @Test
        @DisplayName("문의 사항 전체 조회에 성공한다.")
        public void successGetAllQuestions() {
            // given
            List<Question> questions = getQuestions(currentMember);
            List<QuestionResponseDto> responseData = questions.stream().map(QuestionResponseDto::new).toList();

            // stub
            given(questionRepository.findAll()).willReturn(questions);

            // when
            questionService.getAllQuestions(currentMember);

            // then
            verify(questionRepository).findAll();
            assertThat(responseData).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("문의 사항 단일 조회")
    class getQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static long errorId = -1;
        private final static long questionId = 1;

        @Test
        @DisplayName("유효하지 않은 PK를 통하여 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            willThrow(QuestionNotFoundException.class)
                    .given(questionFindService)
                    .findQuestionById(errorId);

            // when - then
            assertThatThrownBy(() -> questionService.getQuestion(currentMember, errorId))
                    .isInstanceOf(QuestionNotFoundException.class);
        }

        @Test
        @DisplayName("문의 사항 단일 조회에 성공한다.")
        public void successGetQuestion() {
            // given
            Question question = QUESTION_1.getQuestionInstance(currentMember);

            // stub
            given(questionFindService.findQuestionById(questionId)).willReturn(question);

            // when
            QuestionResponseDto responseData = questionService.getQuestion(currentMember, questionId);

            // then
            verify(questionFindService).findQuestionById(questionId);
            assertAll(
                    () -> assertThat(responseData.getTitle()).isEqualTo(question.getTitle()),
                    () -> assertThat(responseData.getContent()).isEqualTo(question.getContent()),
                    () -> assertThat(responseData.getWriter()).isEqualTo(question.getWriter()),
                    () -> assertThat(responseData.getAnswers()).isEmpty()
            );
        }
    }

    @Nested
    @DisplayName("문의 사항 생성")
    class createQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);

        @Test
        @DisplayName("문의 사항 생성에 성공한다.")
        public void successCreateQuestion() {
            // given
            QuestionRequestDto requestDto = getRequestDto(question.getTitle(), question.getContent());

            // stub
            given(questionRepository.save(any())).willReturn(any());

            // when
            questionService.createQuestion(requestDto, currentMember);

            // then
            verify(questionRepository).save(any());
        }
    }

    @Nested
    @DisplayName("문의 사항 수정")
    class editQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member otherMember = MEMBER_2.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static long questionId = 1L;
        private final static long errorId = -1;

        @Test
        @DisplayName("유효하지 않은 PK를 통하여 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            QuestionRequestDto requestDto = getRequestDto(question.getTitle(), question.getContent());

            // stub
            doThrow(QuestionNotFoundException.class)
                    .when(questionFindService)
                            .findQuestionById(errorId);

            // when - then
            assertThatThrownBy(() -> questionService.editQuestion(requestDto, currentMember, errorId))
                    .isInstanceOf(QuestionNotFoundException.class);
        }

        @Test
        @DisplayName("작성자와 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidMember() {
            // given
            QuestionRequestDto requestDto = getRequestDto(question.getTitle(), question.getContent());

            // stub
            given(questionFindService.findQuestionById(questionId)).willReturn(question);

            // when - then
            assertThatThrownBy(() -> questionService.editQuestion(requestDto, otherMember, questionId))
                    .isInstanceOf(QuestionNotMatchedWriterException.class);
        }

        @Test
        @DisplayName("문의 사항 수정에 성공한다.")
        public void successEditQuestion() {
            // given
            QuestionRequestDto requestDto = getRequestDto(question.getTitle(), question.getContent());

            // stub
            given(questionFindService.findQuestionById(questionId)).willReturn(question);

            // when
            questionService.editQuestion(requestDto, currentMember, questionId);

            // then
            verify(questionFindService).findQuestionById(questionId);
        }
    }

    @Nested
    @DisplayName("문의 사항 삭제")
    class deleteQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member otherMember = MEMBER_2.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static long questionId = 1L;
        private final static long errorId = -1;

        @Test
        @DisplayName("유효하지 않은 PK를 통하여 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            doThrow(QuestionNotFoundException.class)
                    .when(questionFindService)
                    .findQuestionById(errorId);

            // when - then
            assertThatThrownBy(() -> questionService.deleteQuestion(currentMember, errorId))
                    .isInstanceOf(QuestionNotFoundException.class);
        }

        @Test
        @DisplayName("작성자와 사용자의 정보가 일치하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidMember() {
            // given

            // stub
            given(questionFindService.findQuestionById(questionId)).willReturn(question);

            // when - then
            assertThatThrownBy(() -> questionService.deleteQuestion(otherMember, questionId))
                    .isInstanceOf(QuestionNotMatchedWriterException.class);
        }

        @Test
        @DisplayName("문의 사항 삭제에 성공한다.")
        public void successDeleteQuestion() {
            // given

            // stub
            given(questionFindService.findQuestionById(questionId)).willReturn(question);

            // when
            questionService.deleteQuestion(currentMember, questionId);

            // then
            verify(questionFindService).findQuestionById(questionId);
            verify(questionRepository).delete(question);
        }
    }

    private List<Question> getQuestions(Member member) {
        List<Question> questions = new ArrayList<>();

        questions.add(QUESTION_1.getQuestionInstance(member));
        questions.add(QUESTION_2.getQuestionInstance(member));
        questions.add(QUESTION_3.getQuestionInstance(member));

        return questions;
    }

    private QuestionRequestDto getRequestDto(String title, String content) {
        return new QuestionRequestDto(title, content);
    }
}