package ro.utcn.sd.alexh.assignment1.persistence.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcAnswerRepository implements AnswerRepository {

    private final JdbcTemplate template;

    @Override
    public Answer save(Answer answer) {
        if (answer.getAnswerId() != null) {
            update(answer);
        } else {
            Integer id = insert(answer);
            answer.setAnswerId(id);
        }
        return answer;
    }

    @Override
    public Optional<Answer> findById(int id) {
        List<Answer> answers = template.query("SELECT * FROM answer WHERE answer_id = ?",
                (resultSet, i) -> new Answer(
                        resultSet.getInt("answer_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("question_id"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("creation_date_time"),
                        resultSet.getInt("score")
                ), id
        );
        return answers.isEmpty() ? Optional.empty() : Optional.of(answers.get(0));
    }

    @Override
    public void remove(Answer answer) {
        template.update("DELETE FROM answer WHERE answer_id = ?", answer.getAnswerId());
    }

    @Override
    public List<Answer> findAll() {
        return template.query("SELECT * FROM answer", (resultSet, i) ->
                new Answer(
                        resultSet.getInt("answer_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("question_id"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("creation_date_time"),
                        resultSet.getInt("score")
                )
        );
    }

    @Override
    public List<Answer> collectAnswersForQuestion(Integer questionId) {
        return template.query("SELECT * FROM answer WHERE answer.question_id = ?",
                (resultSet, i) -> new Answer(
                        resultSet.getInt("answer_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("question_id"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("creation_date_time"),
                        resultSet.getInt("score")
                ), questionId
        );
    }

    private Integer insert(Answer answer) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("answer");
        insert.usingGeneratedKeyColumns("answer_id");

        Map<String, Object> data = new HashMap<>();
        data.put("answer_id", answer.getAnswerId());
        data.put("user_id", answer.getUserId());
        data.put("question_id", answer.getQuestionId());
        data.put("text", answer.getText());
        data.put("creation_date_time", answer.getCreationDateTime());
        data.put("score", answer.getScore());

        return insert.executeAndReturnKey(data).intValue();
    }

    private void update(Answer answer) {
        template.update("UPDATE answer SET text = ?, score = ? WHERE answer_id = ?",
                answer.getText(),
                answer.getScore(),
                answer.getAnswerId());
    }
}
