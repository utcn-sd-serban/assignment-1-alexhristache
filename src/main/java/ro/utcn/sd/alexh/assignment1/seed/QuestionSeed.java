package ro.utcn.sd.alexh.assignment1.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionRepository;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class QuestionSeed implements CommandLineRunner {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        if (questionRepository.findAll().isEmpty()) {
//            questionRepository.save(new Question(1,"Who?", "Who are you?", new Timestamp(System.currentTimeMillis())));
//            questionRepository.save(new Question(2,"Why?", "Why are you here?", new Timestamp(System.currentTimeMillis())));
//            questionRepository.save(new Question(3,"When?", "When are you leaving?", new Timestamp(System.currentTimeMillis())));

            System.out.println(questionRepository.findAll());
        }
    }
}
