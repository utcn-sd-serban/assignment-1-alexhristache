package ro.utcn.sd.alexh.assignment1.persistence.memory;

import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTagRepository implements TagRepository {

    private final Map<Integer, Tag> data = new ConcurrentHashMap<>();
    private volatile int currentId = 1;

    @Override
    public synchronized Tag save(Tag tag) {
        if (tag.getTagId() != null) {
            data.put(tag.getTagId(), tag);
        } else {
            tag.setTagId(currentId++);
            data.put(tag.getTagId(), tag);
        }
        return tag;
    }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void remove(Tag tag) {
        data.remove(tag.getTagId());
    }

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Tag tagToFind = null;

        for (Tag iteratingTag : data.values()) {
            if (iteratingTag.getName().equals(name)) {
                tagToFind = iteratingTag;
                break;
            }
        }
        return Optional.ofNullable(tagToFind);
    }
}
