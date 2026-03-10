package com.duri.duricore.onboarding.service;

import com.duri.duricore.onboarding.model.Choice;
import com.duri.duricore.onboarding.model.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * questions.json 파일을 로드하고 관리하는 서비스
 * 애플리케이션 시작 시 questions.json을 메모리에 로드
 */
@Service
public class QuestionLoaderService {

    private List<Question> questions;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void loadQuestions() {
        try {
            ClassPathResource resource = new ClassPathResource("questions.json");
            InputStream inputStream = resource.getInputStream();
            TypeReference<List<Question>> typeRef = new TypeReference<>() {};
            questions = objectMapper.readValue(inputStream, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load questions.json", e);
        }
    }

    /**
     * 성별에 따른 질문 목록 조회
     * @param gender "MALE" or "FEMALE"
     */
    public List<Question> getQuestionsByGender(String gender) {
        return questions.stream()
                .filter(q -> q.getGender().equals(gender))
                .sorted((q1, q2) -> q1.getQuestionId().compareTo(q2.getQuestionId()))
                .collect(Collectors.toList());
    }

    /**
     * 성별과 질문 ID로 질문 조회
     */
    public Question getQuestionById(String gender, String questionId) {
        return questions.stream()
                .filter(q -> q.getGender().equals(gender) && q.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found: " + questionId + " for gender: " + gender));
    }

    /**
     * 성별, 질문 ID와 선택지 ID로 선택지 조회
     */
    public Choice getChoiceById(String gender, String questionId, String choiceId) {
        Question question = getQuestionById(gender, questionId);
        return question.getChoices().stream()
                .filter(c -> c.getChoiceId().equals(choiceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Choice not found: " + choiceId + " in question: " + questionId));
    }
}
