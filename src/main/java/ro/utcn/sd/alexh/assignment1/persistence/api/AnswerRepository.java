package ro.utcn.sd.alexh.assignment1.persistence.api;

import ro.utcn.sd.alexh.assignment1.entity.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    Answer save(Answer answer);

    Optional<Answer> findById(int id);

    void remove(Answer answer);

    List<Answer> findAll();

    List<Answer> collectAnswersForQuestion(Integer questionId);
}
