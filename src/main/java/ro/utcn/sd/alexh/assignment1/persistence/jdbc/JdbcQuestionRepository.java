package ro.utcn.sd.alexh.assignment1.persistence.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionRepository;

import java.util.*;

@RequiredArgsConstructor
public class JdbcQuestionRepository implements QuestionRepository {

    private final JdbcTemplate template;

    @Override
    public Question save(Question question) {
        if (question.getQuestionId() != null) {
            update(question);
        } else {
            Integer id = insert(question);
            question.setQuestionId(id);
        }

        // Insert also into question_tag, Many-to-Many relationship table
        SimpleJdbcInsert insertQuestionTag = new SimpleJdbcInsert(template);
        insertQuestionTag.setTableName("question_tag");
        insertQuestionTag.usingGeneratedKeyColumns("question_tag_id");
        Map<String, Object> questionTagData = new HashMap<>();
        for (Tag tag : question.getTags()) {
            questionTagData.put("question_id", question.getQuestionId());
            questionTagData.put("tag_id", tag.getTagId());
            insertQuestionTag.executeAndReturnKey(questionTagData);
        }

        return question;
    }

    @Override
    public Optional<Question> findById(int id) {
        List<Question> questions = template.query("SELECT * FROM question WHERE question_id = ?",
                (resultSet, i) -> new Question(
                        resultSet.getInt("question_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("title"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("creation_date_time"),
                        resultSet.getInt("score")
                ), id
        );

        if (questions.isEmpty()) {
            return Optional.empty();
        } else {
            Question question = questions.get(0);
            question.setTags(getTags(question));
            return Optional.of(question);
        }
    }

    @Override
    public void remove(Question question) {
        template.update("DELETE FROM question WHERE question_id = ?", question.getQuestionId());
    }

    @Override
    public List<Question> findAll() {

        List<Question> questionList;
        questionList = template.query("SELECT * FROM question", (resultSet, i) ->
                new Question(
                        resultSet.getInt("question_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("title"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("creation_date_time"),
                        resultSet.getInt("score")
                )
        );

        for (Question question : questionList) {
            question.setTags(getTags(question));
//            question.setScore(getVoteCount(question));
        }

        return questionList;
    }

    private List<Tag> getTags(Question question) {
        return template.query("SELECT * FROM tag WHERE tag_id IN (SELECT question_tag.tag_id FROM question_tag WHERE question_tag.question_id = ?)",
                (resultSet, i) -> new Tag(
                        resultSet.getInt("tag_id"),
                        resultSet.getString("name")
                ), question.getQuestionId()
        );
    }

    private Integer insert(Question question) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("question");
        insert.usingGeneratedKeyColumns("question_id");

        Map<String, Object> data = new HashMap<>();
        data.put("question_id", question.getQuestionId());
        data.put("user_id", question.getUserId());
        data.put("title", question.getTitle());
        data.put("text", question.getText());
        data.put("creation_date_time", question.getCreationDateTime());
        data.put("score", question.getScore());

        return insert.executeAndReturnKey(data).intValue();
    }

    private void update(Question question) {
        template.update("UPDATE question SET title = ?, text = ?, score = ? WHERE question_id = ?",
                question.getTitle(),
                question.getText(),
                question.getScore(),
                question.getQuestionId());
    }
}
