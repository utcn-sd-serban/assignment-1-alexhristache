package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.alexh.assignment1.entity.AnswerVote;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerVoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAnswerVoteRepository implements AnswerVoteRepository {

    private final EntityManager entityManager;

    @Override
    public AnswerVote save(AnswerVote answerVote) {
        if (answerVote.getAnswerVoteId() != null || findByAnswerAndUser(answerVote.getAnswerId(), answerVote.getUserId()).isPresent()) {
            return entityManager.merge(answerVote);
        } else {
            entityManager.persist(answerVote);
            return answerVote;
        }
    }

    @Override
    public Optional<AnswerVote> findById(int id) {
        return Optional.ofNullable(entityManager.find(AnswerVote.class, id));
    }

    @Override
    public void remove(AnswerVote answerVote) {
        entityManager.remove(answerVote);
    }

    @Override
    public Optional<AnswerVote> findByAnswerAndUser(int answerId, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AnswerVote> query = builder.createQuery(AnswerVote.class);
        Root<AnswerVote> root = query.from(AnswerVote.class);

        List<AnswerVote> answerVote = entityManager
                .createQuery(query.select(root)
                        .where(builder.equal(root.get("answerId"), answerId))
                        .where(builder.equal(root.get("userId"), userId)))
                .getResultList();

        return answerVote.isEmpty() ? Optional.empty() : Optional.of(answerVote.get(0));
    }
}
