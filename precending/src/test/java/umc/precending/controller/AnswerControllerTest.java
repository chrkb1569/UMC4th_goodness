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
import umc.precending.controller.answer.AnswerController;
import umc.precending.dto.answer.AnswerRequestDto;
import umc.precending.factory.AnswerFactory;
import umc.precending.service.answer.AnswerService;
import umc.precending.service.member.MemberFindService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static umc.precending.factory.AnswerFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Answer [Controller Layer] -> AnswerController 테스트")
public class AnswerControllerTest {
    @Mock
    private MemberFindService memberFindService;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(answerController)
                .build();
    }

    @Nested
    @DisplayName("답변 생성 API [POST /api/answers]")
    class createAnswerTest {
        private final static AnswerFactory factory = ANSWER_1;
        private final static String BASE_URL = "/api/answers";
        private final static long questionId = 1L;

        @Test
        @DisplayName("답변 생성에 성공한다.")
        public void successCreateAnswer() throws Exception {
            // given
            AnswerRequestDto requestDto = getRequestDto(factory.getContent());

            // stub
            willDoNothing().given(answerService).createAnswer(any(), any(), anyLong());

            // when
            mockMvc.perform(post(BASE_URL)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("questionId", String.valueOf(questionId)))
                    .andExpect(status().isCreated());

            // then
            verify(answerService).createAnswer(any(), any(), anyLong());
        }
    }

    @Nested
    @DisplayName("답변 수정 API [PUT /api/answers/{id}]")
    class editAnswerTest {
        private final static AnswerFactory factory = ANSWER_1;
        private final static String BASE_URL = "/api/answers/{id}";
        private final static long answerId = 1L;

        @Test
        @DisplayName("답변 수정에 성공한다.")
        public void successEditAnswer() throws Exception {
            // given
            AnswerRequestDto requestDto = getRequestDto(factory.getContent());

            // stub
            willDoNothing().given(answerService).editAnswer(any(), any(), anyLong());

            // when
            mockMvc.perform(put(BASE_URL, answerId)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // then
            verify(answerService).editAnswer(any(), any(), anyLong());
        }
    }

    @Nested
    @DisplayName("답변 삭제 API [DELETE /api/answers/{id}]")
    class deleteAnswerTest {
        private final static String BASE_URL = "/api/answers/{id}";
        private final static long answerId = 1L;

        @Test
        @DisplayName("답변 삭제에 성공한다.")
        public void successDeleteAnswer() throws Exception {
            // given

            // stub
            willDoNothing().given(answerService).deleteAnswer(any(), anyLong());

            // when
            mockMvc.perform(delete(BASE_URL, answerId)).andExpect(status().isOk());

            // then
            verify(answerService).deleteAnswer(any(), anyLong());
        }
    }

    private AnswerRequestDto getRequestDto(String content) {
        return new AnswerRequestDto(content);
    }
}