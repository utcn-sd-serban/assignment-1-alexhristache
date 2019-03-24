package ro.utcn.sd.alexh.assignment1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listQuestions() {
        return repositoryFactory.createQuestionRepository().findAll();
    }

    @Transactional
    public Question addQuestion(Integer questionId, Integer userId, String title, String text, Timestamp creationDateTime, List<Tag> tags) {
        return repositoryFactory.createQuestionRepository().save(new Question(questionId, userId, title, text, creationDateTime, tags));
    }
}
