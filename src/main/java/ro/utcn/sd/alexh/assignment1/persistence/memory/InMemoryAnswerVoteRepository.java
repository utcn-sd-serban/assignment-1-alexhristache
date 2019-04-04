package ro.utcn.sd.alexh.assignment1.persistence.memory;

import ro.utcn.sd.alexh.assignment1.entity.AnswerVote;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerVoteRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAnswerVoteRepository implements AnswerVoteRepository {

    private final Map<Integer, AnswerVote> data = new ConcurrentHashMap<>();
    private volatile int currentId = 1;

    @Override
    public synchronized AnswerVote save(AnswerVote answerVote) {
        if (answerVote.getAnswerVoteId() != null) {
            data.put(answerVote.getAnswerVoteId(), answerVote);
        } else if (findByAnswerAndUser(answerVote.getAnswerId(), answerVote.getUserId()).isPresent()) {
            Integer answerVoteId = findByAnswerAndUser(answerVote.getAnswerId(), answerVote.getUserId()).get().getAnswerVoteId();
            answerVote.setAnswerVoteId(answerVoteId);
            data.replace(answerVoteId, answerVote);
        } else {
            answerVote.setAnswerVoteId(currentId++);
            data.put(answerVote.getAnswerVoteId(), answerVote);
        }
        return answerVote;
    }

    @Override
    public Optional<AnswerVote> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<AnswerVote> findByAnswerAndUser(int answerId, int userId) {
        for (AnswerVote answerVote : data.values()) {
            if (answerVote.getAnswerId() == answerId && answerVote.getUserId() == userId) {
                return Optional.of(answerVote);
            }
        }
        return Optional.empty();
    }

    @Override
    public void remove(AnswerVote answerVote) {
        data.remove(answerVote.getAnswerVoteId());
    }
}
