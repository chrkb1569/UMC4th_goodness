package umc.precending.repository.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.precending.domain.answer.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
