package ro.utcn.sd.alexh.assignment1.persistence.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.alexh.assignment1.entity.AnswerVote;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerVoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcAnswerVoteRepository implements AnswerVoteRepository {

    private final JdbcTemplate template;

    @Override
    public AnswerVote save(AnswerVote answerVote) {
        if (answerVote.getAnswerVoteId() != null || findByAnswerAndUser(answerVote.getAnswerId(), answerVote.getUserId()).isPresent()) {
            update(answerVote);
        } else {
            Integer id = insert(answerVote);
            answerVote.setAnswerVoteId(id);
        }
        return answerVote;
    }

    @Override
    public Optional<AnswerVote> findById(int id) {
        List<AnswerVote> answerVotes = template.query("SELECT * FROM answer_vote WHERE answer_vote_id = ?",
                (resultSet, i) -> new AnswerVote(
                        resultSet.getInt("answerVote_id"),
                        resultSet.getInt("answer_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vote")
                ), id
        );
        return answerVotes.isEmpty() ? Optional.empty() : Optional.of(answerVotes.get(0));
    }

    @Override
    public void remove(AnswerVote answerVote) {
        template.update("DELETE FROM answer_vote WHERE answer_vote_id = ?", answerVote.getAnswerVoteId());
    }

    @Override
    public Optional<AnswerVote> findByAnswerAndUser(int answerId, int userId) {
        List<AnswerVote> answerVotes = template.query("SELECT * FROM answer_vote WHERE answer_id = ? AND user_id = ?",
                (resultSet, i) -> new AnswerVote(
                        resultSet.getInt("answer_vote_id"),
                        resultSet.getInt("answer_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vote")
                ), answerId, userId
        );
        return answerVotes.isEmpty() ? Optional.empty() : Optional.of(answerVotes.get(0));
    }

    private void update(AnswerVote answerVote) {
        template.update("UPDATE answer_vote SET vote = ? WHERE answer_vote_id = ?",
                answerVote.getVote(),
                answerVote.getAnswerVoteId()
        );
    }

    private Integer insert(AnswerVote answerVote) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("answer_vote");
        insert.usingGeneratedKeyColumns("answer_vote_id");

        Map<String, Object> data = new HashMap<>();
        data.put("answer_vote_id", answerVote.getAnswerVoteId());
        data.put("answer_id", answerVote.getAnswerId());
        data.put("user_id", answerVote.getUserId());
        data.put("vote", answerVote.getVote());

        return insert.executeAndReturnKey(data).intValue();
    }
}
