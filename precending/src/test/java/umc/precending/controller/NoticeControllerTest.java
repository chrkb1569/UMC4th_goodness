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
import umc.precending.controller.notice.NoticeController;
import umc.precending.domain.notice.Notice;
import umc.precending.dto.notice.NoticeDetailedResponseDto;
import umc.precending.dto.notice.NoticeRequestDto;
import umc.precending.dto.notice.NoticeResponseDto;
import umc.precending.factory.NoticeFactory;
import umc.precending.service.notice.NoticeService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notice [Controller Layer] -> NoticeController 테스트")
public class NoticeControllerTest {
    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(noticeController)
                .build();
    }

    @Nested
    @DisplayName("공지 사항 전체 조회 API [GET /api/notices]")
    class getNoticesTest {
        private final static String BASE_URL = "/api/notices";

        @Test
        @DisplayName("공지 사항 전체 조회에 성공한다.")
        public void successGetNoticesTest() throws Exception {
            // given
            List<NoticeResponseDto> responseData = new ArrayList<>();

            // stub
            given(noticeService.getNotices()).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());

            // then
            verify(noticeService).getNotices();
        }
    }

    @Nested
    @DisplayName("공지 사항 단일 조회 API [GET /api/notices/{id}]")
    class getNoticeTest {
        private final static String BASE_URL = "/api/notices/{id}";
        private final static long noticeId = 1;

        @Test
        @DisplayName("공지 사항 단일 조회에 성공한다.")
        public void successGetNoticeTest() throws Exception {
            // given
            NoticeDetailedResponseDto responseData = new NoticeDetailedResponseDto();

            // stub
            given(noticeService.getNotice(noticeId)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL, noticeId)).andExpect(status().isOk());

            // then
            verify(noticeService).getNotice(noticeId);
        }
    }

    @Nested
    @DisplayName("공지 사항 생성 API [POST /api/admin/notices]")
    class createNoticeTest {
        private final static String BASE_URL = "/api/admin/notices";

        @Test
        @DisplayName("공지 사항 생성에 성공한다.")
        public void successCreateNoticeTest() throws Exception {
            // given
            NoticeRequestDto requestDto = getRequestDto();

            // stub
            willDoNothing().given(noticeService).createNotice(any());

            // when
            mockMvc.perform(post(BASE_URL)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            // then
            verify(noticeService).createNotice(any());
        }
    }

    @Nested
    @DisplayName("공지 사항 수정 API [PUT /api/admin/notices/{id}]")
    class editNoticeTest {
        private final static String BASE_URL = "/api/admin/notices/{id}";
        private final static long noticeId = 1L;

        @Test
        @DisplayName("공지 사항 수정에 성공한다.")
        public void successEditNoticeTest() throws Exception {
            // given
            NoticeRequestDto requestDto = getRequestDto();

            // stub
            willDoNothing().given(noticeService).editNotice(any(), anyLong());

            // when
            mockMvc.perform(put(BASE_URL, noticeId)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // then
            verify(noticeService).editNotice(any(), anyLong());
        }
    }

    @Nested
    @DisplayName("공지 사항 삭제 API [DELETE /api/admin/notices/{id}]")
    class deleteNoticeTest {
        private final static String BASE_URL = "/api/admin/notices/{id}";
        private final static long noticeId = 1L;

        @Test
        @DisplayName("공지 사항 삭제에 성공한다.")
        public void successDeleteNoticeTest() throws Exception {
            // given

            // stub
            willDoNothing().given(noticeService).deleteNotice(noticeId);

            // when
            mockMvc.perform(delete(BASE_URL, noticeId)).andExpect(status().isOk());

            // then
            verify(noticeService).deleteNotice(noticeId);
        }
    }

    private NoticeRequestDto getRequestDto() {
        Notice notice = NoticeFactory.NOTICE_1.getNoticeInstance();

        return new NoticeRequestDto(notice.getTitle(), notice.getContent());
    }
}