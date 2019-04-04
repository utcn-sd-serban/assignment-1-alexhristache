package ro.utcn.sd.alexh.assignment1.persistence.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.persistence.api.TagRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcTagRepository implements TagRepository {

    private final JdbcTemplate template;

    @Override
    public Tag save(Tag tag) {
        if (tag.getTagId() != null) {
            update(tag);
        } else {
            Integer id = insert(tag);
            tag.setTagId(id);
        }
        return tag;
    }

    @Override
    public Optional<Tag> findById(int id) {
        List<Tag> tags = template.query("SELECT * FROM tag WHERE tag_id = ?",
                (resultSet, i) -> new Tag(
                        resultSet.getInt("tag_id"),
                        resultSet.getString("name")
                ), id
        );
        return tags.isEmpty() ? Optional.empty() : Optional.of(tags.get(0));
    }

    @Override
    public void remove(Tag tag) {
        template.update("DELETE FROM tag WHERE tag_id = ?", tag.getTagId());
    }

    @Override
    public List<Tag> findAll() {
        return template.query("SELECT * FROM tag", (resultSet, i) ->
                new Tag(
                        resultSet.getInt("tag_id"),
                        resultSet.getString("name")
                )
        );
    }

    @Override
    public Optional<Tag> findByName(String name) {
        List<Tag> tags = template.query("SELECT * FROM tag WHERE name = ?",
                (resultSet, i) -> new Tag(
                        resultSet.getInt("tag_id"),
                        resultSet.getString("name")
                ), name
        );
        return tags.isEmpty() ? Optional.empty() : Optional.of(tags.get(0));
    }

    private Integer insert(Tag tag) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("tag");
        insert.usingGeneratedKeyColumns("tag_id");

        Map<String, Object> data = new HashMap<>();
        data.put("tag_id", tag.getTagId());
        data.put("name", tag.getName());

        return insert.executeAndReturnKey(data).intValue();
    }

    private void update(Tag tag) {
        template.update("UPDATE tag SET name = ?, text = ? WHERE tag_id = ?",
                tag.getName(),
                tag.getTagId());
    }
}
