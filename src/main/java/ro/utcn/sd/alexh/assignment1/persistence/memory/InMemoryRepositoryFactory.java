package ro.utcn.sd.alexh.assignment1.persistence.memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.alexh.assignment1.persistence.api.*;

@Component
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "MEMORY")
public class InMemoryRepositoryFactory implements RepositoryFactory {

    private final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    private final InMemoryAnswerRepository answerRepository = new InMemoryAnswerRepository();
    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private final InMemoryTagRepository tagRepository = new InMemoryTagRepository();
    private final InMemoryQuestionVoteRepository questionVoteRepository = new InMemoryQuestionVoteRepository();
    private final InMemoryAnswerVoteRepository answerVoteRepository = new InMemoryAnswerVoteRepository();

    @Override
    public QuestionRepository createQuestionRepository() {
        return questionRepository;
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return answerRepository;
    }

    @Override
    public UserRepository createUserRepository() {
        return userRepository;
    }

    @Override
    public TagRepository createTagRepository() {
        return tagRepository;
    }

    @Override
    public QuestionVoteRepository createQuestionVoteRepository() {
        return questionVoteRepository;
    }

    @Override
    public AnswerVoteRepository createAnswerVoteRepository() {
        return answerVoteRepository;
    }
}
