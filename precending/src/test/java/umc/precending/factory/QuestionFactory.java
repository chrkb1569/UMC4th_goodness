package umc.precending.factory;

import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;

public enum QuestionFactory {
    QUESTION_1("QUESTION_TITLE_1", "QUESTION_CONTENT_1"),
    QUESTION_2("QUESTION_TITLE_2", "QUESTION_CONTENT_2"),
    QUESTION_3("QUESTION_TITLE_3", "QUESTION_CONTENT_3")
    ;

    private String title;
    private String content;
    QuestionFactory(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Question getQuestionInstance(Member member) {
        String writer = member.getUsername();

        return new Question(title, content, writer);
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }
}