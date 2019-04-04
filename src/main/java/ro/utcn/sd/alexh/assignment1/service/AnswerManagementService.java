package ro.utcn.sd.alexh.assignment1.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.entity.AnswerVote;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.exception.AnswerNotFoundException;
import ro.utcn.sd.alexh.assignment1.exception.IllegalUserOperationException;
import ro.utcn.sd.alexh.assignment1.exception.SelfVoteException;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Answer addAnswer(Integer answerId, Integer userId, Integer questionId, String text, Timestamp creationDateTime, int score) {
        return repositoryFactory.createAnswerRepository().save(new Answer(answerId, userId, questionId, text, creationDateTime, score));
    }

    @Transactional
    public Answer updateAnswer(Answer answer) {
        return repositoryFactory.createAnswerRepository().save(answer);
    }

    @Transactional
    public List<Answer> listAnswers() {
        return repositoryFactory.createAnswerRepository().findAll();
    }

    @Transactional
    public void getAnswersForQuestion(List<Question> questionList) {

        for (Question question : questionList) {
            List<Answer> answers = repositoryFactory.createAnswerRepository().collectAnswersForQuestion(question.getQuestionId());
            answers.sort(Comparator.comparing(Answer::getScore).reversed());
            question.setAnswers(answers);
        }
    }

    @Transactional
    public void deleteAnswer(Integer userId, Integer answerId) throws IllegalUserOperationException {
        Optional<Answer> maybeAnswer = repositoryFactory.createAnswerRepository().findById(answerId);
        if (maybeAnswer.isPresent()) {
            Answer answer = maybeAnswer.get();
            if (answer.getUserId().equals(userId)) {
                repositoryFactory.createAnswerRepository().remove(answer);
            } else {
                throw new IllegalUserOperationException();
            }
        } else {
            throw new AnswerNotFoundException();
        }
    }

    @Transactional
    public void editAnswer(Integer userId, Integer answerId, String text) {
        Optional<Answer> maybeAnswer = repositoryFactory.createAnswerRepository().findById(answerId);
        if (maybeAnswer.isPresent()) {
            Answer answer = maybeAnswer.get();
            if (answer.getUserId().equals(userId)) {
                answer.setText(text);
                repositoryFactory.createAnswerRepository().save(answer);
            } else {
                throw new IllegalUserOperationException();
            }
        } else {
            throw new AnswerNotFoundException();
        }
    }

    @Transactional
    public Answer findAnswerById(Integer id) {
        return repositoryFactory.createAnswerRepository().findById(id).orElseThrow(AnswerNotFoundException::new);
    }

    @Transactional
    public void addVote(AnswerVote answerVote) {
        if (answerVote.getUserId().equals(findAnswerById(answerVote.getAnswerId()).getUserId())) {
            throw new SelfVoteException();
        }
        Answer answer = findAnswerById(answerVote.getAnswerId());
        answer.setScore(answer.getScore() + answerVote.getVote());
        updateAnswer(answer);
    }

    @Transactional
    public void removeVote(AnswerVote answerVote) {
        Answer answer = findAnswerById(answerVote.getAnswerId());
        answer.setScore(answer.getScore() - answerVote.getVote());
        updateAnswer(answer);
    }
}
