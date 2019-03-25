package ro.utcn.sd.alexh.assignment1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.utcn.sd.alexh.assignment1.entity.Question;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.exception.AnswerNotFoundException;
import ro.utcn.sd.alexh.assignment1.exception.QuestionNotFoundException;
import ro.utcn.sd.alexh.assignment1.exception.UserAlreadyExists;
import ro.utcn.sd.alexh.assignment1.exception.UserNotFoundException;
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
            case "exit":
                return true;
            default:
                System.out.println("Unknown command. Try again.");
                return false;
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
        String title;
        User currentUser;
        String text;
        Timestamp creationDateTime;
        List<Tag> tags = new LinkedList<>();

        if (userManagementService.getLoggedUser() == null) {
            System.out.println("Please log in before posting a question.");
            return;
        }

        title = input("Title = ");
        String[] stringTags = input("Tags (separated by <,>): ").split("\\s*,\\s*");
        for (String stringTag : stringTags) {
            Tag tag = tagManagementService.addTag(null, stringTag);
            tags.add(tag);
        }
        currentUser = userManagementService.getLoggedUser();
        text = input("Write your question below:");
        creationDateTime = new Timestamp(System.currentTimeMillis());
        questionManagementService.addQuestion(null, currentUser.getUserId(), title, text, creationDateTime, tags);

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
                "[Id=" + question.getQuestionId() + "] " + "\"" + question.getTitle() + "\"" + "\n"
                + "Posted by " + userManagementService.findUserById(question.getUserId()).getUsername() + "\n"
                + "Tags: " + question.getTags() + "\n"
                + "\"" + question.getText() + "\"" + "\n"
                + question.getCreationDateTime() + "\n"
                + "Answers: \n" + question.getAnswers()
        );
    }

    private void printQuestionList(List<Question> questionList) {
        for (Question question : questionList) {
            printQuestion(question);
        }
    }
}
