package umc.precending.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.precending.domain.notice.Notice;
import umc.precending.dto.notice.NoticeDetailedResponseDto;
import umc.precending.dto.notice.NoticeRequestDto;
import umc.precending.dto.notice.NoticeResponseDto;
import umc.precending.exception.notice.NoticeNotFoundException;
import umc.precending.repository.notice.NoticeRepository;
import umc.precending.service.notice.NoticeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static umc.precending.factory.NoticeFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notice [Service Layer] -> NoticeService 테스트")
public class NoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Nested
    @DisplayName("공지 사항 전체 조회")
    class getNoticesTest {
        @Test
        @DisplayName("조회한 데이터가 존재하지 않을 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<Notice> responseData = new ArrayList<>();

            // stub
            given(noticeRepository.findAll()).willReturn(responseData);

            // when - then
            assertThatThrownBy(() -> noticeService.getNotices())
                    .isInstanceOf(NoticeNotFoundException.class);
        }

        @Test
        @DisplayName("공지 사항 전체 조회에 성공한다.")
        public void successGetNotices() {
            // given
            List<Notice> notices = getNoticeList();

            // stub
            given(noticeRepository.findAll()).willReturn(notices);

            // when
            List<NoticeResponseDto> responseData = noticeService.getNotices();

            // then
            verify(noticeRepository).findAll();
            assertThat(responseData).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("공지 사항 단일 조회")
    class getNoticeTest {
        private final static Notice notice = NOTICE_1.getNoticeInstance();
        private final static long errorId = -1;
        private final static long noticeId = 1;

        @Test
        @DisplayName("유효하지 않은 PK로 요청할 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            given(noticeRepository.findById(errorId)).willReturn(Optional.ofNullable(null));

            // when - then
            assertThatThrownBy(() -> noticeService.getNotice(errorId))
                    .isInstanceOf(NoticeNotFoundException.class);
        }

        @Test
        @DisplayName("공지 사항 단일 조회에 성공한다.")
        public void successGetNotice() {
            // given
            final int INITIAL_VIEW_COUNT = notice.getViewCount();

            // stub
            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            // when
            NoticeDetailedResponseDto responseData = noticeService.getNotice(noticeId);

            // then
            assertAll(
                    () -> assertThat(responseData.getTitle()).isEqualTo(notice.getTitle()),
                    () -> assertThat(responseData.getContent()).isEqualTo(notice.getContent()),
                    () -> assertThat(responseData.getViewCount()).isGreaterThan(INITIAL_VIEW_COUNT)
            );
        }
    }

    @Nested
    @DisplayName("공지 사항 생성")
    class createNoticeTest {
        private final static Notice notice = NOTICE_1.getNoticeInstance();

        @Test
        @DisplayName("공지 사항 생성에 성공한다.")
        public void successCreateNotice() {
            // given
            NoticeRequestDto createDto = getRequestDto(notice);

            // stub
            given(noticeRepository.save(any())).willReturn(any());

            // when
            noticeService.createNotice(createDto);

            // then
            verify(noticeRepository).save(any());
        }
    }

    @Nested
    @DisplayName("공지 사항 수정")
    class editNoticeTest {
        private final static Notice notice = NOTICE_1.getNoticeInstance();
        private final static long errorId = -1;
        private final static long noticeId = 1;

        @Test
        @DisplayName("유효하지 않은 PK로 요청할 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            NoticeRequestDto requestDto = getRequestDto(notice);

            // stub
            given(noticeRepository.findById(errorId)).willReturn(Optional.ofNullable(null));

            // when - then
            assertThatThrownBy(() -> noticeService.editNotice(requestDto, errorId))
                    .isInstanceOf(NoticeNotFoundException.class);
        }

        @Test
        @DisplayName("공지 사항 수정에 성공한다.")
        public void successEditNotice() {
            // given
            NoticeRequestDto requestDto = getRequestDto(notice);

            // stub
            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            // when
            noticeService.editNotice(requestDto, noticeId);

            // then
            assertAll(
                    () -> assertThat(notice.getTitle()).isEqualTo(requestDto.getTitle()),
                    () -> assertThat(notice.getContent()).isEqualTo(requestDto.getContent())
            );
        }
    }

    @Nested
    @DisplayName("공지 사항 삭제")
    class deleteNoticeTest {
        private final static Notice notice = NOTICE_1.getNoticeInstance();
        private final static long errorId = -1;
        private final static long noticeId = 1;

        @Test
        @DisplayName("유효하지 않은 PK로 요청할 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            given(noticeRepository.findById(errorId)).willReturn(Optional.ofNullable(null));

            // when - then
            assertThatThrownBy(() -> noticeService.deleteNotice(errorId))
                    .isInstanceOf(NoticeNotFoundException.class);
        }

        @Test
        @DisplayName("공지 사항 삭제에 성공한다.")
        public void successDeleteNotice() {
            // given

            // stub
            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            // when
            noticeService.deleteNotice(noticeId);

            // then
            verify(noticeRepository).delete(notice);
        }
    }

    private List<Notice> getNoticeList() {
        List<Notice> notices = new ArrayList<>();

        notices.add(NOTICE_1.getNoticeInstance());
        notices.add(NOTICE_2.getNoticeInstance());

        return notices;
    }

    private NoticeRequestDto getRequestDto(Notice notice) {
        return new NoticeRequestDto(notice.getTitle(), notice.getContent());
    }
}