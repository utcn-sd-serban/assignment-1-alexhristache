package ro.utcn.sd.alexh.assignment1.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AnswerVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerVoteId;
    private Integer answerId;
    private Integer userId;
    private int vote;
}
