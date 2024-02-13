package umc.precending.service.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.answer.Answer;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.dto.answer.AnswerRequestDto;
import umc.precending.exception.answer.AnswerNotFoundException;
import umc.precending.exception.answer.AnswerNotMatchedWriterException;
import umc.precending.repository.answer.AnswerRepository;
import umc.precending.service.question.QuestionFindService;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final QuestionFindService questionFindService;
    private final AnswerRepository answerRepository;

    @Transactional
    public void createAnswer(AnswerRequestDto requestDto, Member member, Long questionId) {
        Question findQuestion = getQuestionById(questionId);
        Answer answer = getInstance(requestDto.getContent(), member, findQuestion);
        findQuestion.addAnswer(answer);

        answerRepository.save(answer);
    }

    @Transactional
    public void editAnswer(AnswerRequestDto requestDto, Member member, Long answerId) {
        Answer findAnswer = findAnswerById(answerId);

        checkMemberValidation(findAnswer, member);

        findAnswer.editContent(requestDto.getContent());
    }

    @Transactional
    public void deleteAnswer(Member member, Long answerId) {
        Answer findAnswer = findAnswerById(answerId);

        checkMemberValidation(findAnswer, member);

        answerRepository.delete(findAnswer);
    }

    private Question getQuestionById(long questionId) {
        return questionFindService.findQuestionById(questionId);
    }

    private Answer getInstance(String content, Member member, Question question) {
        return new Answer(content, member.getUsername(), question);
    }

    private Answer findAnswerById(long answerId) {
        return answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
    }

    private void checkMemberValidation(Answer answer, Member member) {
        String writer = member.getUsername();

        if(!answer.getWriter().equals(writer)) throw new AnswerNotMatchedWriterException();
    }
}
