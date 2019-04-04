package ro.utcn.sd.alexh.assignment1.persistence.api;

public interface RepositoryFactory {

    QuestionRepository createQuestionRepository();

    AnswerRepository createAnswerRepository();

    UserRepository createUserRepository();

    TagRepository createTagRepository();

    QuestionVoteRepository createQuestionVoteRepository();

    AnswerVoteRepository createAnswerVoteRepository();
}
