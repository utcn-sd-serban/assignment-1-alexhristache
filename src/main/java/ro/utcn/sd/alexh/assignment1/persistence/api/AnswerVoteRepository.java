package ro.utcn.sd.alexh.assignment1.persistence.api;

import ro.utcn.sd.alexh.assignment1.entity.AnswerVote;

import java.util.Optional;

public interface AnswerVoteRepository {

    AnswerVote save(AnswerVote answerVote);

    Optional<AnswerVote> findById(int id);

    Optional<AnswerVote> findByAnswerAndUser(int answerId, int userId);

    void remove(AnswerVote answerVote);
}
