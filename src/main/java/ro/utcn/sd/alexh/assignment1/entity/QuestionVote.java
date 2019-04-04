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
public class QuestionVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionVoteId;
    private Integer questionId;
    private Integer userId;
    private int vote;
}
