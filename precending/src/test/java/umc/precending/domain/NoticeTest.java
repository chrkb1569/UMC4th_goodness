package umc.precending.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.precending.domain.notice.Notice;
import umc.precending.factory.NoticeFactory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static umc.precending.factory.NoticeFactory.*;

@DisplayName("Notice 도메인 테스트")
public class NoticeTest {
    private Notice notice;

    @Test
    @DisplayName("Notice Domain 객체를 생성한다.")
    public void createInstanceTest() {
        // given
        NoticeFactory noticeFactory = NOTICE_1;

        // when
        notice = noticeFactory.getNoticeInstance();

        // then
        assertAll(
                () -> assertThat(notice.getContent()).isEqualTo(noticeFactory.getContent()),
                () -> assertThat(notice.getTitle()).isEqualTo(noticeFactory.getTitle()),
                () -> assertThat(notice.getViewCount()).isEqualTo(noticeFactory.getViewCount())
        );
    }

    @Test
    @DisplayName("updateViewCount() 메서드를 테스트한다.")
    public void updateViewCountTest() {
        // given
        final int INITIAL_VIEW_COUNT = 0;
        notice = NOTICE_1.getNoticeInstance();

        // when
        notice.updateViewCount();

        // then
        assertThat(notice.getViewCount()).isNotEqualTo(INITIAL_VIEW_COUNT);
    }

    @Test
    @DisplayName("editNotice() 메서드를 테스트한다.")
    public void editNoticeTest() {
        // given
        NoticeFactory otherNoticeFactory = NOTICE_2;
        notice = NOTICE_1.getNoticeInstance();

        // when
        notice.editNotice(otherNoticeFactory.getTitle(), otherNoticeFactory.getContent());

        // then
        assertAll(
                () -> assertThat(notice.getTitle()).isEqualTo(otherNoticeFactory.getTitle()),
                () -> assertThat(notice.getContent()).isEqualTo(otherNoticeFactory.getContent())
        );
    }
}