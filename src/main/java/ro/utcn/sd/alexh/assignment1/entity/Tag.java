package ro.utcn.sd.alexh.assignment1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    }

    @Override
    public String toString() {
        return name;
    }
}
