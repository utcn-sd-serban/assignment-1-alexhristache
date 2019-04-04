package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.persistence.api.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateUserRepository  implements UserRepository {

    private final EntityManager entityManager;

    @Override
    public User save(User user) {
        if (user.getUserId() != null) {
            return entityManager.merge(user);
        } else {
            entityManager.persist(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public void remove(User user) {
        entityManager.remove(user);
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);

        query.select(query.from(User.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<User> user = entityManager.createQuery(query.select(root).where(builder.equal(root.get("username"), username))).getResultList();
        return user.isEmpty() ? Optional.empty() : Optional.ofNullable(user.get(0));
    }
}
