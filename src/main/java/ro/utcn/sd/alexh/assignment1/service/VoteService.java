package ro.utcn.sd.alexh.assignment1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.AnswerVote;
import ro.utcn.sd.alexh.assignment1.entity.QuestionVote;
import ro.utcn.sd.alexh.assignment1.exception.AlreadyVotedException;
import ro.utcn.sd.alexh.assignment1.exception.AnswerVoteNotFoundException;
import ro.utcn.sd.alexh.assignment1.exception.QuestionVoteNotFoundException;
import ro.utcn.sd.alexh.assignment1.persistence.api.AnswerVoteRepository;
import ro.utcn.sd.alexh.assignment1.persistence.api.QuestionVoteRepository;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public QuestionVote addQuestionVote(Integer questionId, Integer userId, int vote) {

        Optional<QuestionVote> maybeVote = repositoryFactory.createQuestionVoteRepository().findByQuestionAndUser(questionId, userId);
        if (maybeVote.isPresent() && maybeVote.get().getVote() == vote) {
            throw new AlreadyVotedException();
        } else {
            QuestionVote questionVote = new QuestionVote(null, questionId, userId, vote);
            return repositoryFactory.createQuestionVoteRepository().save(questionVote);
        }
    }

    @Transactional
    public AnswerVote addAnswerVote(Integer answerId, Integer userId, int vote) {

        Optional<AnswerVote> maybeVote = repositoryFactory.createAnswerVoteRepository().findByAnswerAndUser(answerId, userId);
        if (maybeVote.isPresent() && maybeVote.get().getVote() == vote) {
            throw new AlreadyVotedException();
        } else {
            AnswerVote answerVote = new AnswerVote(null, answerId, userId, vote);
            return repositoryFactory.createAnswerVoteRepository().save(answerVote);
        }
    }

    @Transactional
    public QuestionVote removeQuestionVote(Integer questionId, Integer userId) {
        QuestionVoteRepository questionVoteRepository = repositoryFactory.createQuestionVoteRepository();
        Optional<QuestionVote> maybeQuestionVote = questionVoteRepository.findByQuestionAndUser(questionId, userId);

        if (maybeQuestionVote.isPresent()) {
            questionVoteRepository.remove(maybeQuestionVote.get());
            return maybeQuestionVote.get();
        } else {
            throw new QuestionVoteNotFoundException();
        }
    }

    @Transactional
    public AnswerVote removeAnswerVote(Integer answerId, Integer userId) {
        AnswerVoteRepository answerVoteRepository = repositoryFactory.createAnswerVoteRepository();
        Optional<AnswerVote> maybeAnswerVote = answerVoteRepository.findByAnswerAndUser(answerId, userId);

        if (maybeAnswerVote.isPresent()) {
            answerVoteRepository.remove(maybeAnswerVote.get());
            return maybeAnswerVote.get();
        } else {
            throw new AnswerVoteNotFoundException();
        }
    }

    @Transactional
    public QuestionVote findQuestionVoteByQuestionAndUser(Integer questionId, Integer userId) {
        return repositoryFactory.createQuestionVoteRepository().findByQuestionAndUser(questionId, userId).orElseThrow(QuestionVoteNotFoundException::new);
    }

    @Transactional
    public AnswerVote findAnswerVoteByAnswerAndUser(Integer answerId, Integer userId) {
        return repositoryFactory.createAnswerVoteRepository().findByAnswerAndUser(answerId, userId).orElseThrow(AnswerVoteNotFoundException::new);
    }
}
