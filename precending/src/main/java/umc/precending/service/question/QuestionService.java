package umc.precending.service.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.member.Member;
import umc.precending.domain.question.Question;
import umc.precending.dto.question.QuestionListDto;
import umc.precending.dto.question.QuestionRequestDto;
import umc.precending.dto.question.QuestionResponseDto;
import umc.precending.exception.question.QuestionNotFoundException;
import umc.precending.exception.question.QuestionNotMatchedWriterException;
import umc.precending.repository.question.QuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionFindService questionFindService;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public List<QuestionListDto> getAllQuestions(Member member) {
        List<Question> questions = questionRepository.findAll();

        if(questions.isEmpty()) throw new QuestionNotFoundException();

        return questions.stream().map(QuestionListDto::new).toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestion(Member member, Long questionId) {
        Question findQuestion = findQuestionById(questionId);

        return new QuestionResponseDto(findQuestion);
    }

    @Transactional
    public void createQuestion(QuestionRequestDto requestDto, Member findMember) {
        Question question = getInstance(requestDto, findMember);
        questionRepository.save(question);
    }

    @Transactional
    public void editQuestion(QuestionRequestDto requestDto, Member findMember, Long questionId) {
        Question findQuestion = findQuestionById(questionId);

        checkMemberValidation(findQuestion, findMember);

        findQuestion.editQuestion(requestDto.getTitle(), requestDto.getContent());
    }

    @Transactional
    public void deleteQuestion(Member findMember, Long questionId) {
        Question findQuestion = findQuestionById(questionId);

        checkMemberValidation(findQuestion, findMember);

        questionRepository.delete(findQuestion);
    }

    private Question getInstance(QuestionRequestDto requestDto, Member member) {
        return new Question(requestDto.getTitle(), requestDto.getContent(), member.getUsername());
    }

    private Question findQuestionById(Long questionId) {
        return questionFindService.findQuestionById(questionId);
    }

    private void checkMemberValidation(Question question, Member member) {
        String writer = question.getWriter();

        if(!writer.equals(member.getUsername())) throw new QuestionNotMatchedWriterException();
    }
}
