package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ro.utcn.sd.alexh.assignment1.persistence.api.*;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "JPA")
public class HibernateRepositoryFactory implements RepositoryFactory {

    private final EntityManager entityManager;

    @Override
    public QuestionRepository createQuestionRepository() {
        return new HibernateQuestionRepository(entityManager);
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return new HibernateAnswerRepository(entityManager);
    }

    @Override
    public UserRepository createUserRepository() {
        return new HibernateUserRepository(entityManager);
    }

    @Override
    public TagRepository createTagRepository() {
        return new HibernateTagRepository(entityManager);
    }

    @Override
    public QuestionVoteRepository createQuestionVoteRepository() {
        return new HibernateQuestionVoteRepository(entityManager);
    }

    @Override
    public AnswerVoteRepository createAnswerVoteRepository() {
        return new HibernateAnswerVoteRepository(entityManager);
    }
}
