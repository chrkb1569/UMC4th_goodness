package umc.precending.service.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.question.Question;
import umc.precending.exception.question.QuestionNotFoundException;
import umc.precending.repository.question.QuestionRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionFindService {
    private final QuestionRepository questionRepository;

    public Question findQuestionById(long questionId) {
        return questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
    }
}