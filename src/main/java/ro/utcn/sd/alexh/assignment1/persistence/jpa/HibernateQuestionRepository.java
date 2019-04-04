package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateQuestionRepository implements QuestionRepository {

    private final EntityManager entityManager;

    @Override
    public Question save(Question question) {
        if (question.getQuestionId() != null) {
            return entityManager.merge(question);
        } else {
            entityManager.persist(question);
            return question;
        }
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(entityManager.find(Question.class, id));
    }

    @Override
    public void remove(Question question) {
        entityManager.remove(question);
    }

    @Override
    public List<Question> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);

        query.select(query.from(Question.class));
        return entityManager.createQuery(query).getResultList();
    }
}
