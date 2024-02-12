package umc.precending.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.notice.Notice;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetailedResponseDto {
    private Long id;
    private String title;
    private String content;
    private int viewCount;

    public NoticeDetailedResponseDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.viewCount = notice.getViewCount();
    }
}