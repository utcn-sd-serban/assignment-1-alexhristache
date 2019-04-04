package ro.utcn.sd.alexh.assignment1.persistence.memory;

import ro.utcn.sd.alexh.assignment1.entity.QuestionVote;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionVoteRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryQuestionVoteRepository implements QuestionVoteRepository {

    private final Map<Integer, QuestionVote> data = new ConcurrentHashMap<>();
    private volatile int currentId = 1;

    @Override
    public synchronized QuestionVote save(QuestionVote questionVote) {
        if (questionVote.getQuestionVoteId() != null) {
            data.put(questionVote.getQuestionVoteId(), questionVote);
        } else if (findByQuestionAndUser(questionVote.getQuestionId(), questionVote.getUserId()).isPresent()) {
            Integer questionVoteId = findByQuestionAndUser(questionVote.getQuestionId(), questionVote.getUserId()).get().getQuestionVoteId();
            questionVote.setQuestionVoteId(questionVoteId);
            data.replace(questionVoteId, questionVote);
        } else {
            questionVote.setQuestionVoteId(currentId++);
            data.put(questionVote.getQuestionVoteId(), questionVote);
        }
        return questionVote;
    }

    @Override
    public Optional<QuestionVote> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<QuestionVote> findByQuestionAndUser(int questionId, int userId) {
        for (QuestionVote questionVote : data.values()) {
            if (questionVote.getQuestionId() == questionId && questionVote.getUserId() == userId) {
                return Optional.of(questionVote);
            }
        }
        return Optional.empty();
    }

    @Override
    public void remove(QuestionVote questionVote) {
        data.remove(questionVote.getQuestionVoteId());
    }
}
