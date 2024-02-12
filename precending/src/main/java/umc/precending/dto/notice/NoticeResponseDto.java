package umc.precending.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.notice.Notice;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String title;
    private int viewCount;

    public static NoticeResponseDto toDto(Notice notice) {
        return new NoticeResponseDto(notice.getId(), notice.getContent(), notice.getViewCount());
    }
}