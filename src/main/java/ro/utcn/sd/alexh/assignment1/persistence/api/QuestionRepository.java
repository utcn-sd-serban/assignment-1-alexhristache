package ro.utcn.sd.alexh.assignment1.persistence.api;

import ro.utcn.sd.alexh.assignment1.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Question save(Question question);

    Optional<Question> findById(int id);

    void remove(Question question);

    List<Question> findAll();

}
