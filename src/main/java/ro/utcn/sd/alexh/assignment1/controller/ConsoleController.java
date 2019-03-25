package ro.utcn.sd.alexh.assignment1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.utcn.sd.alexh.assignment1.entity.Answer;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.exception.*;
import ro.utcn.sd.alexh.assignment1.service.AnswerManagementService;
import ro.utcn.sd.alexh.assignment1.service.QuestionManagementService;
import ro.utcn.sd.alexh.assignment1.service.TagManagementService;
import ro.utcn.sd.alexh.assignment1.service.UserManagementService;

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

    @Override
    public void run(String... args) throws Exception {
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
            case "exit":
                return true;
            default:
                System.out.println("Unknown command. Try again.");
                return false;
        }
    }

    private void handleEditAnswer() {
        Optional<User> maybeUser = userManagementService.getLoggedUser();
        if (maybeUser.isPresent()) {
            Integer answerId = Integer.parseInt(input("Answer id = "));
            String newText = input("Write your edited answer: ");

            try {
                answerManagementService.editAnswer(maybeUser.get().getUserId(), answerId, newText);
            } catch (IllegalUserOperationException e) {
                System.out.println("You can only edit answers you posted!");
            } catch (AnswerNotFoundException e) {
                System.out.println("Answer not found.");
            }
        } else {
            System.out.println("Please log in before editing an answer.");
        }
    }

    private void handleRemoveAnswer() {
        Optional<User> maybeUser = userManagementService.getLoggedUser();
        if (maybeUser.isPresent()) {
            Integer answerId = Integer.parseInt(input("Answer id = "));
            try {
                answerManagementService.deleteAnswer(maybeUser.get().getUserId(), answerId);
            } catch (IllegalUserOperationException e) {
                System.out.println("You can only delete answers you posted!");
            } catch (AnswerNotFoundException e) {
                System.out.println("Answer not found.");
            }
        } else {
            System.out.println("Please log in before removing an answer.");
        }

    }

    private void handleAddAnswer() {
        if (userManagementService.getLoggedUser().isPresent()) {
            Integer questionId = Integer.parseInt(input("Question id = "));
            String text = input("Your answer: ");
            answerManagementService.addAnswer(null,
                    userManagementService.getLoggedUser().get().getUserId(),
                    questionId, text,
                    new Timestamp(System.currentTimeMillis()));
            System.out.println("Answer posted successfully");
        } else {
            System.out.println("Please log in before posting a question.");
        }
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

        if (userManagementService.login(username, password)) {
            System.out.println("Welcome, " + username);
        } else {
            System.out.println("Login failed");
        }
    }

    private void handleRegisterUser() {
        String email;
        String username = "";
        String password;
        boolean usernameAlreadyExists = false;

        email = input("Email = ");

        do {
            try {
                username = input("Username = ");
                usernameAlreadyExists = false;
                password = input("Password = ");
                userManagementService.addUser(null, email, username, password, "regular", 0, false);
            } catch (UserAlreadyExists e) {
                System.out.println("This username already exists.");
                usernameAlreadyExists = true;
            }
        } while (usernameAlreadyExists);

        System.out.println("User " + username + " was registered successfully.");
    }

    private void handleAddQuestion() {
        if (userManagementService.getLoggedUser().isPresent()) {
            String title;
            User currentUser;
            String text;
            Timestamp creationDateTime;
            List<Tag> tags = new LinkedList<>();

            title = input("Title = ");
            String[] stringTags = input("Tags (separated by <,>): ").split("\\s*,\\s*");
            for (String stringTag : stringTags) {
                Tag tag = tagManagementService.addTag(null, stringTag);
                tags.add(tag);
            }
            currentUser = userManagementService.getLoggedUser().get();
            text = input("Your question: ");
            creationDateTime = new Timestamp(System.currentTimeMillis());
            questionManagementService.addQuestion(null, currentUser.getUserId(), title, text, creationDateTime, tags);

            System.out.println("Your question was added successfully.");
        } else {
            System.out.println("Please log in before posting a question.");
        }
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
                "[Id=" + question.getQuestionId() + "] " + "\"" + question.getTitle() + "\"" + "\n"
                + "Posted by " + userManagementService.findUserById(question.getUserId()).getUsername() + "\n"
                + "Tags: " + question.getTags() + "\n"
                + (char)27 + "[33m" + "\"" + question.getText() + "\"" + (char)27 + "[0m" + "\n"
                + question.getCreationDateTime() + "\n"
                + "Answers:"
        );
        printAnswerList(question.getAnswers());
    }

    private void printAnswer(Answer answer) {
        System.out.println(
                "\t" +  "[Id=" + answer.getAnswerId() + "]" + "\n"
                + "\t"+ "Posted by " + userManagementService.findUserById(answer.getUserId()).getUsername() + "\n"
                + "\t" + (char)27 + "[35m" + "\"" + answer.getText() + "\"" + (char)27 + "[0m" + "\n"
                + "\t" +  answer.getCreationDateTime() + "\n"
        );
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

    private void printQuestionList(List<Question> questionList) {
        answerManagementService.getAnswersForQuestion(questionList);
        for (Question question : questionList) {
            printQuestion(question);
        }
    }
}
