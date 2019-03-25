package ro.utcn.sd.alexh.assignment1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listQuestions() {
        List<Question> questionList = repositoryFactory.createQuestionRepository().findAll();

        // Order them by creationDatTime
        questionList.sort(Comparator.comparing(Question::getCreationDateTime).reversed());

        return  questionList;
    }

    @Transactional
    public Question addQuestion(Integer questionId, Integer userId, String title, String text, Timestamp creationDateTime, List<Tag> tags) {
        return repositoryFactory.createQuestionRepository().save(new Question(questionId, userId, title, text, creationDateTime, tags));
    }

    @Transactional
    public List<Question> listQuestionsByTag(String tagName) {
        List<Question> allQuestions = listQuestions();
        List<Question> filteredQuestions = new LinkedList<>();

        for (Question question : allQuestions) {
            for (Tag iteratingTag : question.getTags()) {
                if (iteratingTag.getName().equals(tagName)) {
                    filteredQuestions.add(question);
                    break;
                }
            }
        }

        return filteredQuestions;
    }

    @Transactional
    public List<Question> listQuestionsByText(String text) {
        List<Question> allQuestions = listQuestions();
        List<Question> filteredQuestions = new LinkedList<>();

        for (Question question : allQuestions) {
            if (question.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredQuestions.add(question);
            }
        }

        return filteredQuestions;
    }
}
