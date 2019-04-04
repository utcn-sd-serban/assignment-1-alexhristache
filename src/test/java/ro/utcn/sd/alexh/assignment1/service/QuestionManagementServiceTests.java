package ro.utcn.sd.alexh.assignment1.service;

import org.junit.Assert;
import org.junit.Test;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionRepository;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;
import ro.utcn.sd.alexh.assignment1.persistence.api.UserRepository;
import ro.utcn.sd.alexh.assignment1.persistence.memory.InMemoryRepositoryFactory;

import java.sql.Timestamp;
import java.util.Arrays;

public class QuestionManagementServiceTests {

    private static final RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();
    private static final UserRepository userRepository = repositoryFactory.createUserRepository();
    private static final QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();

    static {
        userRepository.save(new User(1, "email@email.com", "username1", "password1", "regular", 0, false));
        questionRepository.save(new Question(1, 1, "Title1", "Question 1 text",
                new Timestamp(System.currentTimeMillis()),
                Arrays.asList(new Tag(1, "Tag1"), new Tag(2, "Tag2")), 0));
        questionRepository.save(new Question(2, 1, "Title2", "Question 2 text",
                new Timestamp(System.currentTimeMillis()),
                Arrays.asList(new Tag(1, "Tag1"), new Tag(4, "Tag4")), 0));
    }

    @Test
    public void addQuestionTest() {

        questionRepository.save(new Question(123, 1, "Title123", "Question 123 text",
                new Timestamp(System.currentTimeMillis()),
                Arrays.asList(new Tag(1, "Tag1"), new Tag(2, "Tag2"), new Tag(3, "Tag3")), 0));

        Question question = new Question(123, 1, "Title123", "Question 123 text",
                new Timestamp(System.currentTimeMillis()),
                Arrays.asList(new Tag(1, "Tag1"), new Tag(2, "Tag2"), new Tag(3, "Tag3")), 0);

        Question questionInRepo = questionRepository.findById(123).get();

        Assert.assertEquals(questionInRepo, question);
    }

    @Test
    public void findAllTest() {
        Assert.assertEquals(questionRepository.findAll().size(), 2);
    }

    @Test
    public void remove() {
        int sizeBefore = questionRepository.findAll().size();

        questionRepository.save(new Question(10, 1, "Title10", "Question 10 text",
                new Timestamp(System.currentTimeMillis()),
                Arrays.asList(new Tag(1, "Tag1"), new Tag(4, "Tag4")), 0));

        questionRepository.remove(questionRepository.findById(10).get());

        int sizeAfter = questionRepository.findAll().size();

        Assert.assertEquals(sizeBefore, sizeAfter);
    }
}
