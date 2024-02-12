package umc.precending.factory;

import umc.precending.domain.notice.Notice;

public enum NoticeFactory {
    NOTICE_1("Notice Title_1", "Notice Content_1", 0),
    NOTICE_2("Notice Title_2", "Notice Content_2", 0)
    ;

    private String title;
    private String content;
    private int viewCount;

    NoticeFactory(String title, String content, int viewCount) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
    }

    public Notice getNoticeInstance() {
        return new Notice(this.title, this.content);
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public int getViewCount() {
        return this.viewCount;
    }
}