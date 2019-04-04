package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.alexh.assignment1.entity.QuestionVote;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionVoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateQuestionVoteRepository implements QuestionVoteRepository {

    private final EntityManager entityManager;

    @Override
    public QuestionVote save(QuestionVote questionVote) {
        if (questionVote.getQuestionVoteId() != null || findByQuestionAndUser(questionVote.getQuestionId(), questionVote.getUserId()).isPresent()) {
            return entityManager.merge(questionVote);
        } else {
            entityManager.persist(questionVote);
            return questionVote;
        }
    }

    @Override
    public Optional<QuestionVote> findById(int id) {
        return Optional.ofNullable(entityManager.find(QuestionVote.class, id));
    }

    @Override
    public void remove(QuestionVote questionVote) {
        entityManager.remove(questionVote);
    }

    @Override
    public Optional<QuestionVote> findByQuestionAndUser(int questionId, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<QuestionVote> query = builder.createQuery(QuestionVote.class);
        Root<QuestionVote> root = query.from(QuestionVote.class);

        List<QuestionVote> questionVote = entityManager
                .createQuery(query.select(root)
                        .where(builder.equal(root.get("questionId"), questionId))
                        .where(builder.equal(root.get("userId"), userId)))
                .getResultList();

        return questionVote.isEmpty() ? Optional.empty() : Optional.of(questionVote.get(0));
    }
}
