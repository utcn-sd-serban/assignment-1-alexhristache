package ro.utcn.sd.alexh.assignment1.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Answer> listAnswers() {
        return repositoryFactory.createAnswerRepository().findAll();
    }

    @Transactional
    public Answer addAnswer(Integer answerId, Integer userId, String text, Timestamp creationDateTime) {
        return repositoryFactory.createAnswerRepository().save(new Answer(answerId, userId, text, creationDateTime));
    }
}
