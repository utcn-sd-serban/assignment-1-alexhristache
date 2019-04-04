package ro.utcn.sd.alexh.assignment1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.exception.*;
import ro.utcn.sd.alexh.assignment1.service.*;

import java.sql.Timestamp;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ConsoleController implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final QuestionManagementService questionManagementService;
    private final UserManagementService userManagementService;
    private final TagManagementService tagManagementService;
    private final AnswerManagementService answerManagementService;
    private final VoteService voteService;

    @Override
    public void run(String... args) {
        System.out.println("\nWelcome to the Poorly Implemented Stack Overflow system!\n");
        handleListQuestions();

        boolean done = false;
        while (!done) {
            System.out.println("\nEnter a command: ");
            String command = scanner.nextLine().trim();
            try {
                done = handleCommand(command);
            } catch (QuestionNotFoundException e) {
                System.out.println("The question with the given ID was not found!");
            } catch (AnswerNotFoundException e) {
                System.out.println("The answer with the given ID was not found!");
            } catch (UserNotFoundException e) {
                System.out.println("The user with the given ID was not found!");
            } catch (UserAlreadyExistsException e) {
                System.out.println("An user with this name already exists!");
            } catch (LoginFailedException e) {
                System.out.println("Login failed. Please try again!");
            } catch (IllegalUserOperationException e) {
                System.out.println("You can only edit answers you posted!");
            } catch (UserNotLoggedException e) {
                System.out.println("You need to log in first!");
            } catch (AlreadyVotedException e) {
                System.out.println("You have already voted this!");
            } catch (QuestionVoteNotFoundException e) {
                System.out.println("Question Vote not found!");
            } catch (AnswerVoteNotFoundException e) {
                System.out.println("Answer Vote not found!");
            } catch (SelfVoteException e) {
                System.out.println("You cannot vote your posts!");
            }
        }
    }

    private boolean handleCommand(String command) {
        System.out.println("Your command = " + command);
        switch (command) {
            case "list questions":
                handleListQuestions();
                return false;
            case "add question":
                handleAddQuestion();
                return false;
            case "register":
                handleRegisterUser();
                return false;
            case "login":
                handleLogin();
                return false;
            case "logout":
                handleLogout();
                return false;
            case "list questions -tag":
                handleListQuestionsByTag();
                return false;
            case "list questions -text":
                handleListQuestionsByText();
                return false;
            case "add answer":
                handleAddAnswer();
                return false;
            case "remove answer":
                handleRemoveAnswer();
                return false;
            case "edit answer":
                handleEditAnswer();
                return false;
            case "upvote question":
                handleVoteQuestion(1);
                return false;
            case "downvote question":
                handleVoteQuestion(-1);
                return false;
            case "upvote answer":
                handleVoteAnswer(1);
                return false;
            case "downvote answer":
                handleVoteAnswer(-1);
                return false;
            case "remove question vote":
                handleRemoveQuestionVote();
                return false;
            case "remove answer vote":
                handleRemoveAnswerVote();
                return false;
            case "exit":
                return true;
            default:
                System.out.println("Unknown command. Try again.");
                return false;
        }
    }

    private void handleRemoveAnswerVote() {
        User loggedUser = userManagementService.getLoggedUser();
        Integer answerId = Integer.parseInt(input("Answer id = "));
        answerManagementService.findAnswerById(answerId); // Check if valid question
        answerManagementService.removeVote(voteService.removeAnswerVote(answerId, loggedUser.getUserId()));
    }

    private void handleRemoveQuestionVote() {
        User loggedUser = userManagementService.getLoggedUser();
        Integer questionId = Integer.parseInt(input("Question id = "));
        questionManagementService.findQuestionById(questionId); // Check if valid question
        questionManagementService.removeVote(voteService.removeQuestionVote(questionId, loggedUser.getUserId()));
    }

    private void handleVoteAnswer(int vote) {
        User loggedUser = userManagementService.getLoggedUser();
        Integer answerId = Integer.parseInt(input("Answer id = "));
        answerManagementService.findAnswerById(answerId); // Check if valid question
        try {
            answerManagementService.removeVote(voteService.removeAnswerVote(answerId, loggedUser.getUserId()));
        } catch (AnswerVoteNotFoundException ignored) {
        }
        answerManagementService.addVote(voteService.addAnswerVote(answerId, loggedUser.getUserId(), vote));
    }

    private void handleVoteQuestion(int vote) {
        User loggedUser = userManagementService.getLoggedUser();
        Integer questionId = Integer.parseInt(input("Question id = "));
        questionManagementService.findQuestionById(questionId); // Check if valid question
        try {
            questionManagementService.removeVote(voteService.removeQuestionVote(questionId, loggedUser.getUserId()));
        } catch (QuestionVoteNotFoundException ignored) {
        }
        questionManagementService.addVote(voteService.addQuestionVote(questionId, loggedUser.getUserId(), vote));
    }

    private void handleEditAnswer() {
        User loggedUser = userManagementService.getLoggedUser();
        Integer answerId = Integer.parseInt(input("Answer id = "));
        String newText = input("Write your edited answer: ");
        answerManagementService.editAnswer(loggedUser.getUserId(), answerId, newText);
    }

    private void handleRemoveAnswer() {
        User loggedUser = userManagementService.getLoggedUser();
        Integer answerId = Integer.parseInt(input("Answer id = "));
        answerManagementService.deleteAnswer(loggedUser.getUserId(), answerId);
    }

    private void handleAddAnswer() {
        User loggedUser = userManagementService.getLoggedUser();
        Integer questionId = Integer.parseInt(input("Question id = "));
        String text = input("Your answer: ");
        answerManagementService.addAnswer(null,
                loggedUser.getUserId(),
                questionId, text,
                new Timestamp(System.currentTimeMillis()),
                0);
        System.out.println("Answer posted successfully");
    }

    private void handleListQuestionsByText() {
        String text = input("Text = ").toLowerCase();
        printQuestionList(questionManagementService.listQuestionsByText(text));
    }

    private void handleListQuestionsByTag() {
        String tag = input("Tag = ").trim();
        printQuestionList(questionManagementService.listQuestionsByTag(tag));
    }

    private void handleLogout() {
        userManagementService.logout();
        System.out.println("Logged out.");
    }

    private void handleLogin() {
        String username = input("Username = ");
        String password = input("Password = ");
        userManagementService.login(username, password);
    }

    private void handleRegisterUser() {
        String email = input("Email = ");
        String username = input("Username = ");
        String password = input("Password = ");
        userManagementService.addUser(null, email, username, password, "regular", 0, false);

        System.out.println("User " + username + " was registered successfully.");
    }

    private void handleAddQuestion() {
        User loggedUser = userManagementService.getLoggedUser();
        List<Tag> tags = new LinkedList<>();
        String title = input("Title = ");
        String[] stringTags = input("Tags (separated by <,>): ").split("\\s*,\\s*");
        String text = input("Your question: ");
        Timestamp creationDateTime = new Timestamp(System.currentTimeMillis());

        for (String stringTag : stringTags) {
            Tag tag = tagManagementService.addTag(null, stringTag);
            tags.add(tag);
        }

        questionManagementService.addQuestion(null, loggedUser.getUserId(), title, text, creationDateTime, tags, 0);

        System.out.println("Your question was added successfully.");
    }

    private void handleListQuestions() {
        printQuestionList(questionManagementService.listQuestions());
    }

    private String input(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private void printQuestion(Question question) {
        System.out.println(
                "[QuestionId=" + question.getQuestionId() + "] " + "\"" + question.getTitle() + "\"" + "\n"
                + "Posted by " + userManagementService.findUserById(question.getUserId()).getUsername() + "\n"
                + "Tags: " + question.getTags() + "\n"
                + "Score: " + question.getScore() + "\n"
                + (char)27 + "[33m" + "\"" + question.getText() + "\"" + (char)27 + "[0m" + "\n"
                + question.getCreationDateTime() + "\n"
                + "Answers:"
        );
        printAnswerList(question.getAnswers());
    }

    private void printAnswer(Answer answer) {
        System.out.println(
                "\t" +  "[AnswerId=" + answer.getAnswerId() + "]" + "\n"
                + "\t" + "Posted by " + userManagementService.findUserById(answer.getUserId()).getUsername() + "\n"
                + "\t" + "Score: " + answer.getScore() + "\n"
                + "\t" + (char)27 + "[35m" + "\"" + answer.getText() + "\"" + (char)27 + "[0m" + "\n"
                + "\t" +  answer.getCreationDateTime() + "\n"
        );
    }

    private void printQuestionList(List<Question> questionList) {
        answerManagementService.getAnswersForQuestion(questionList);
        for (Question question : questionList) {
            printQuestion(question);
        }
    }

    private void printAnswerList(List<Answer> answerList) {
        if (answerList.isEmpty()) {
            System.out.println("<empty>\n");
        } else {
            for (Answer answer : answerList) {
                printAnswer(answer);
            }
        }
    }
}
