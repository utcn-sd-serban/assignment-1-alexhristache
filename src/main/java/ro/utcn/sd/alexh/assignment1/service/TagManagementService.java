package ro.utcn.sd.alexh.assignment1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Tag> listTags() {
        return repositoryFactory.createTagRepository().findAll();
    }

    @Transactional
    public Tag addTag(Integer tagId, String name) {

        Integer existingTagId = findTagIdByName(name);

        if (existingTagId == null) { // If the tag does not exist
            return repositoryFactory.createTagRepository().save(new Tag(tagId, name));
        } else {
            return new Tag(existingTagId, name); // Tag exists, only return the object
        }
    }

    private Integer findTagIdByName(String name) {
        Optional<Tag> maybeTag = repositoryFactory.createTagRepository().findByName(name);
        return maybeTag.map(Tag::getTagId).orElse(null);
    }
}
