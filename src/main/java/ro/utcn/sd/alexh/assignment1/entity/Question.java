package ro.utcn.sd.alexh.assignment1.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;
    private Integer userId;
    private String title;
    private String text;
    private Timestamp creationDateTime;
    private int score;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private List<Answer> answers;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    public Question(Integer questionId, Integer userId, String title, String text, Timestamp creationDateTime, List<Tag> tags, int score) {
        this.questionId = questionId;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.creationDateTime = creationDateTime;
        this.answers = new LinkedList<>();
        this.tags = tags;
        this.score = score;
    }

    public Question(Integer questionId, Integer userId, String title, String text, Timestamp creationDateTime, int score) {
        this.questionId = questionId;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.creationDateTime = creationDateTime;
        this.answers = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.score = score;
    }
}
