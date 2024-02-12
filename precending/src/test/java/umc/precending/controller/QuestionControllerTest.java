package umc.precending.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import umc.precending.controller.question.QuestionController;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.dto.question.QuestionListDto;
import umc.precending.dto.question.QuestionRequestDto;
import umc.precending.dto.question.QuestionResponseDto;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.question.QuestionService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.QuestionFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Question [Controller Layer] -> QuestionController 테스트")
public class QuestionControllerTest {
    @Mock
    private MemberFindService memberFindService;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(questionController)
                .build();
    }

    @Nested
    @DisplayName("전체 문의 사항 조회 API [GET /api/questions]")
    class getQuestionsTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/questions";

        @Test
        @DisplayName("전체 문의 사항 조회에 성공한다.")
        public void successGetQuestions() throws Exception {
            // given
            List<QuestionListDto> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(questionService.getAllQuestions(currentMember)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());

            // then
            verify(questionService).getAllQuestions(currentMember);
        }
    }

    @Nested
    @DisplayName("단일 문의 사항 조회 API [GET /api/questions/{id}]")
    class getQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static String BASE_URL = "/api/questions/{id}";
        private final static long questionId = 1L;

        @Test
        @DisplayName("단일 문의 사항 조회에 성공한다.")
        public void successGetQuestion() throws Exception {
            // given
            QuestionResponseDto responseData = new QuestionResponseDto(question);

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(questionService.getQuestion(currentMember, questionId)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL, questionId)).andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(questionService).getQuestion(currentMember, questionId);
        }
    }

    @Nested
    @DisplayName("문의 사항 생성 API [POST /api/questions]")
    class createQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static String BASE_URL = "/api/questions";

        @Test
        @DisplayName("문의 사항 생성에 성공한다.")
        public void successCreateQuestion() throws Exception {
            // given
            QuestionRequestDto requestDto = getRequestDto(question.getTitle(), question.getContent());

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(questionService).createQuestion(any(), any());

            // when
            mockMvc.perform(post(BASE_URL)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            // then
            verify(memberFindService).findCurrentMember();
            verify(questionService).createQuestion(any(), any());
        }
    }

    @Nested
    @DisplayName("문의 사항 수정 API [PUT /api/questions/{id}]")
    class editQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static Question question = QUESTION_1.getQuestionInstance(currentMember);
        private final static String BASE_URL = "/api/questions/{id}";
        private final static long questionId = 1L;

        @Test
        @DisplayName("문의 사항 수정에 성공한다.")
        public void successEditQuestion() throws Exception {
            // given
            QuestionRequestDto requestDto = getRequestDto(question.getTitle(), question.getContent());

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(questionService).editQuestion(any(), any(), anyLong());

            // when
            mockMvc.perform(put(BASE_URL, questionId)
                    .content(objectMapper.writeValueAsBytes(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(questionService).editQuestion(any(), any(), anyLong());
        }
    }

    @Nested
    @DisplayName("문의 사항 삭제 API [DELETE /api/questions/{id}]")
    class deleteQuestionTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/questions/{id}";
        private final static long questionId = 1L;

        @Test
        @DisplayName("문의 사항 삭제에 성공한다.")
        public void successDeleteQuestion() throws Exception {
            // given

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            willDoNothing().given(questionService).deleteQuestion(currentMember, questionId);

            // when
            mockMvc.perform(delete(BASE_URL, questionId))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(questionService).deleteQuestion(currentMember, questionId);
        }
    }

    private QuestionRequestDto getRequestDto(String title, String content) {
        return new QuestionRequestDto(title, content);
    }
}