package ro.utcn.sd.alexh.assignment1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.QuestionVote;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.exception.QuestionNotFoundException;
import ro.utcn.sd.alexh.assignment1.exception.SelfVoteException;
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

        return questionList;
    }

    @Transactional
    public Question addQuestion(Integer questionId, Integer userId, String title, String text, Timestamp creationDateTime, List<Tag> tags, int score) {
        return repositoryFactory.createQuestionRepository().save(new Question(questionId, userId, title, text, creationDateTime, tags, score));
    }

    @Transactional
    public Question updateQuestion(Question question) {
        return repositoryFactory.createQuestionRepository().save(question);
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

    @Transactional
    public Question findQuestionById(Integer id) {
        return repositoryFactory.createQuestionRepository().findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    public void addVote(QuestionVote questionVote) {

        if (questionVote.getUserId().equals(findQuestionById(questionVote.getQuestionId()).getUserId())) {
            throw new SelfVoteException();
        }

        Question question = findQuestionById(questionVote.getQuestionId());
        question.setScore(question.getScore() + questionVote.getVote());
        updateQuestion(question);
    }

    @Transactional
    public void removeVote(QuestionVote questionVote) {
        Question question = findQuestionById(questionVote.getQuestionId());
        question.setScore(question.getScore() - questionVote.getVote());
        updateQuestion(question);
    }
}
