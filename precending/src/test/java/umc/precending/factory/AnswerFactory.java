package umc.precending.factory;

import umc.precending.domain.answer.Answer;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;

public enum AnswerFactory {
    ANSWER_1("ANSWER_CONTENT_1"),
    ANSWER_2("ANSWER_CONTENT_2"),
    ANSWER_3("ANSWER_CONTENT_3")
    ;

    private String content;

    AnswerFactory(String content) {
        this.content = content;
    }

    public Answer getAnswerInstance(Member member, Question question) {
        String writer = member.getUsername();

        return new Answer(content, writer, question);
    }

    public String getContent() {
        return this.content;
    }
}