package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAnswerRepository implements AnswerRepository {

    private final EntityManager entityManager;

    @Override
    public Answer save(Answer answer) {
        if (answer.getAnswerId() != null) {
            return entityManager.merge(answer);
        } else {
            entityManager.persist(answer);
            return answer;
        }
    }

    @Override
    public Optional<Answer> findById(int id) {
        return Optional.ofNullable(entityManager.find(Answer.class, id));
    }

    @Override
    public void remove(Answer answer) {
        entityManager.remove(answer);
    }

    @Override
    public List<Answer> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);

        query.select(query.from(Answer.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Answer> collectAnswersForQuestion(Integer questionId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        Root<Answer> root = query.from(Answer.class);

        return entityManager.createQuery(query.select(root).where(builder.equal(root.get("questionId"), questionId))).getResultList();
    }
}
