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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String email;
    private String username;
    private String password;
    private String type;
    private int score;
    private boolean isBanned;
}
