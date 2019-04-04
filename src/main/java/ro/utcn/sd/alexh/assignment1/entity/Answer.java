package ro.utcn.sd.alexh.assignment1.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;
    private Integer userId;
    @Column(name = "question_id")
    private Integer questionId;
    private String text;
    private Timestamp creationDateTime;
    private int score;
}
