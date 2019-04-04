package ro.utcn.sd.alexh.assignment1.persistence.memory;

import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAnswerRepository implements AnswerRepository {

    private final Map<Integer, Answer> data = new ConcurrentHashMap<>();
    private volatile int currentId = 1;

    @Override
    public synchronized Answer save(Answer answer) {
        if (answer.getAnswerId() != null) {
            data.put(answer.getAnswerId(), answer);
        } else {
            answer.setAnswerId(currentId++);
            data.put(answer.getAnswerId(), answer);
        }
        return answer;
    }

    @Override
    public Optional<Answer> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void remove(Answer answer) {
        data.remove(answer.getAnswerId());
    }

    @Override
    public List<Answer> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Answer> collectAnswersForQuestion(Integer questionId) {

        List<Answer> answerList = new LinkedList<>();

        for (Answer answer : data.values()) {
            if (answer.getQuestionId().equals(questionId)) {
                answerList.add(answer);
            }
        }

        return answerList;
    }
}
