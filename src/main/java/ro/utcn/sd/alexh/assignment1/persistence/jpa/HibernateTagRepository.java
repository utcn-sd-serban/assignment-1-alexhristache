package ro.utcn.sd.alexh.assignment1.persistence.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.TagRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateTagRepository implements TagRepository {

    private final EntityManager entityManager;

    @Override
    public Tag save(Tag tag) {
        if (tag.getTagId() != null) {
            return entityManager.merge(tag);
        } else {
            entityManager.persist(tag);
            return tag;
        }
    }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public void remove(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public List<Tag> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);

        query.select(query.from(Tag.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);

        List<Tag> tag = entityManager.createQuery(query.select(root).where(builder.equal(root.get("name"), name))).getResultList();
        return tag.isEmpty() ? Optional.empty() : Optional.ofNullable(tag.get(0));
    }
}
