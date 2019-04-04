package ro.utcn.sd.alexh.assignment1.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.Tag;
import ro.utcn.sd.alexh.assignment1.service.AnswerManagementService;
import ro.utcn.sd.alexh.assignment1.service.QuestionManagementService;
import ro.utcn.sd.alexh.assignment1.service.TagManagementService;
import ro.utcn.sd.alexh.assignment1.service.UserManagementService;

import java.sql.Timestamp;
import java.util.*;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "MEMORY")
public class Seed implements CommandLineRunner {

    private final QuestionManagementService questionService;
    private final TagManagementService tagService;
    private final UserManagementService userService;
    private final AnswerManagementService answerService;
    private final int GEN_COUNT = 10;

    @Transactional
    @Override
    public void run(String... args) {

        if (tagService.listTags().isEmpty()) {
            for (int i = 0; i < GEN_COUNT; i++) {
                tagService.addTag(i, "Tag" + i);
            }
        }

        if (userService.listUsers().isEmpty()) {
            for (int i = 0; i < GEN_COUNT; i++) {
                userService.addUser(i, i + "@email.com", "user" + i, "123", "regular", 0, false);
            }
        }

        if (questionService.listQuestions().isEmpty()) {
            for (int i = 0; i < GEN_COUNT; i++) {
                questionService.addQuestion(i, i, "Title" + i, "Text " + i, new Timestamp(System.currentTimeMillis()),
                        Collections.singletonList(tagService.findTagById(i)), 0);
            }
        }

        if (answerService.listAnswers().isEmpty()) {
            for (int i = 0; i < GEN_COUNT; i++) {
                answerService.addAnswer(i, i, i, "Answer text " + i, new Timestamp(System.currentTimeMillis()),0);
            }
        }
    }
}
