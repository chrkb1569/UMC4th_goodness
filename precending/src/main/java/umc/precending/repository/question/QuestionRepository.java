package umc.precending.repository.question;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.precending.domain.question.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
