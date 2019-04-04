package ro.utcn.sd.alexh.assignment1.persistence.memory;

import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<Integer, Question> data = new ConcurrentHashMap<>();
    private volatile int currentId = 1;

    /**
     * Save question into memory or update it.
     *
     * @param question the question.
     * @return the created question.
     */
    @Override
    public synchronized Question save(Question question) {
        if (question.getQuestionId() != null) {
            data.put(question.getQuestionId(), question);
        } else {
            question.setQuestionId(currentId++);
            data.put(question.getQuestionId(), question);
        }
        return question;
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public synchronized void remove(Question question) {
        data.remove(question.getQuestionId());
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(data.values());
    }
}
