package ro.utcn.sd.alexh.assignment1.persistence.api;

import ro.utcn.sd.alexh.assignment1.entity.QuestionVote;

import java.util.Optional;

public interface QuestionVoteRepository {

    QuestionVote save(QuestionVote questionVote);

    Optional<QuestionVote> findById(int id);

    Optional<QuestionVote> findByQuestionAndUser(int questionId, int userId);

    void remove(QuestionVote questionVote);
}
