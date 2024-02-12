package umc.precending.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.base.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "NOTICE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title; // 공지사항 제목

    @Lob
    @Column(name = "content", nullable = false)
    private String content; // 공지사항 내용

    @Column(name = "viewCount", nullable = false)
    private int viewCount; // 공지사항 조회수

    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
    }

    public void updateViewCount() {
        this.viewCount++;
    }

    public void editNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }
}