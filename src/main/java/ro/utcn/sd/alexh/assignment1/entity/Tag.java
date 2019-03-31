package ro.utcn.sd.alexh.assignment1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;
    private String name;
    @ManyToMany(mappedBy = "tags")
    private List<Question> questions;

    public Tag(Integer tagId, String name) {
        this.tagId = tagId;
        this.name = name;
        this.questions = new LinkedList<>();
    }

    @Override
    public String toString() {
        return name;
    }
}
