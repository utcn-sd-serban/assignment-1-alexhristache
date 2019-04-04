package ro.utcn.sd.alexh.assignment1.persistence.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.alexh.assignment1.entity.QuestionVote;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionVoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcQuestionVoteRepository implements QuestionVoteRepository {

    private final JdbcTemplate template;

    @Override
    public QuestionVote save(QuestionVote questionVote) {
        if (questionVote.getQuestionVoteId() != null || findByQuestionAndUser(questionVote.getQuestionId(), questionVote.getUserId()).isPresent()) {
            update(questionVote);
        } else {
            Integer id = insert(questionVote);
            questionVote.setQuestionVoteId(id);
        }
        return questionVote;
    }

    @Override
    public Optional<QuestionVote> findById(int id) {
        List<QuestionVote> questionVotes = template.query("SELECT * FROM question_vote WHERE question_vote_id = ?",
                (resultSet, i) -> new QuestionVote(
                        resultSet.getInt("questionVote_id"),
                        resultSet.getInt("question_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vote")
                ), id
        );
        return questionVotes.isEmpty() ? Optional.empty() : Optional.of(questionVotes.get(0));
    }

    @Override
    public void remove(QuestionVote questionVote) {
        template.update("DELETE FROM question_vote WHERE question_vote_id = ?", questionVote.getQuestionVoteId());
    }

    @Override
    public Optional<QuestionVote> findByQuestionAndUser(int questionId, int userId) {
        List<QuestionVote> questionVotes = template.query("SELECT * FROM question_vote WHERE question_id = ? AND user_id = ?",
                (resultSet, i) -> new QuestionVote(
                        resultSet.getInt("question_vote_id"),
                        resultSet.getInt("question_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vote")
                ), questionId, userId
        );
        return questionVotes.isEmpty() ? Optional.empty() : Optional.of(questionVotes.get(0));
    }

    private void update(QuestionVote questionVote) {
        template.update("UPDATE question_vote SET vote = ? WHERE question_vote_id = ?",
                questionVote.getVote(),
                questionVote.getQuestionVoteId()
        );
    }

    private Integer insert(QuestionVote questionVote) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("question_vote");
        insert.usingGeneratedKeyColumns("question_vote_id");

        Map<String, Object> data = new HashMap<>();
        data.put("question_vote_id", questionVote.getQuestionVoteId());
        data.put("question_id", questionVote.getQuestionId());
        data.put("user_id", questionVote.getUserId());
        data.put("vote", questionVote.getVote());

        return insert.executeAndReturnKey(data).intValue();
    }
}
