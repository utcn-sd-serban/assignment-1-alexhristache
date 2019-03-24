package ro.utcn.sd.alexh.assignment1.persistence.api;

import ro.utcn.sd.alexh.assignment1.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    Optional<Tag> findById(int id);

    Optional<Tag> findByName(String name);

    void remove(Tag tag);

    List<Tag> findAll();
}
